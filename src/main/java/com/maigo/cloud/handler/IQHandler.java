package com.maigo.cloud.handler;

import com.maigo.cloud.model.User;
import com.maigo.cloud.network.Session;
import com.maigo.cloud.service.MessageService;
import com.maigo.cloud.service.NotificationService;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.UserService;
import com.maigo.cloud.xmpp.*;

import java.util.UUID;

public class IQHandler implements StanzaHandler
{
    private final static String SERVER_NAME = "MaigoCloudService";

    public void handle(String stanza, Session session)
    {
        if(stanza.startsWith("<iq"))
        {
            IQ iq = new IQ();
            iq.parse(stanza);
            String nodeName = iq.getChildNodeName();
            String xmlns = iq.getChildXmlns();

            if (nodeName.equals("bind") && xmlns.equals(IQBind.DEFAULT_XMLNS) && session.isStartedBind())
            {
                IQBind iqBind = new IQBind();
                iqBind.parse(stanza);

                bindResourceWithSession(session, iqBind);
            }
            else if (nodeName.equals("session") && xmlns.equals(IQSession.DEFAULT_XMLNS) && session.isResourceBound())
            {
                IQSession iqSession = new IQSession();
                iqSession.parse(stanza);

                replyRequestSession(session, iqSession);
            }
            else if (nodeName.equals("query") && xmlns.equals("jabber:iq:roster") && session.isSessionRequested())
            {
                replyRosterQuery(session, iq);
            }
            else if (nodeName.equals("query") && xmlns.equals("jabber:iq:register"))
            {
                IQQueryRegister iqQueryRegister = new IQQueryRegister();
                iqQueryRegister.parse(stanza);
                handleUserRegister(session, iqQueryRegister);
            }
            else if (nodeName.equals("ack") && xmlns.equals(IQAck.DEFAULT_XMLNS))
            {
                IQAck iqAck = new IQAck();
                iqAck.parse(stanza);
                handleAck(iqAck);
            }
            else if (nodeName.equals("ping") && xmlns.equals(IQPing.DEFAULT_XMLNS))
            {
                IQPing iqPing = new IQPing();
                iqPing.parse(stanza);
                handlePing(session, iqPing);
            }
            else if (nodeName.equals("alias") && xmlns.equals(IQAlias.DEFAULT_XMLNS))
            {
                IQAlias iqAlias = new IQAlias();
                iqAlias.parse(stanza);
                handleSetAlias(session, iqAlias);
            }
        }
    }

    public String getStanzaStartsWith()
    {
        return "<iq";
    }

    private void bindResourceWithSession(Session session, IQBind iqBind)
    {
        iqBind.setType("result");

        iqBind.setJid(session.getUser().getUsername() + "@" + SERVER_NAME + "/" + iqBind.getResource());
        iqBind.setResource(null);   //should set to null or the <resource/> tag will be output

        session.setState(Session.STATE_RESOURCE_BOUND);
        session.sendStanza(iqBind);
    }

    private void replyRequestSession(Session session, IQSession iqSession)
    {
        IQ iq = new IQ(null, null);
        iq.setId(iqSession.getId());
        iq.setType("result");

        session.setState(Session.STATE_SESSION_REQUESTED);
        session.sendStanza(iq);
    }

    private void replyRosterQuery(Session session, IQ iq)
    {
        //roster is useless for the Cloud Service. always return an empty roster
        iq.setType("result");

        session.setState(Session.STATE_ACTIVE);

        User user = session.getUser();
        System.out.println("User login!");
        System.out.println("username = " + user.getUsername());
        System.out.println("password = " + user.getPassword());
        System.out.println("apiKey = " + user.getApiKey());

        session.sendStanza(iq);

        //Send the off-line notifications and transparent messages to the user since the user has just authenticated
        NotificationService notificationService = (NotificationService)ServiceManager.getInstance().getService("notificationService");
        notificationService.sendOfflineNotificationsToUser(user);

        MessageService messageService = (MessageService)ServiceManager.getInstance().getService("messageService");
        messageService.sendOfflineMessagesToUser(user);
    }

    private void handleUserRegister(Session session, IQQueryRegister iqQueryRegister)
    {
        User user = new User();
        user.setUsername(iqQueryRegister.getUsername());
        user.setPassword(iqQueryRegister.getPassword());
        user.setApiKey(iqQueryRegister.getApiKey());

        UserService userService = (UserService) ServiceManager.getInstance().getService("userService");
        boolean isRegisterSuccess = userService.registerUser(user);

        if(isRegisterSuccess)
        {
            IQ emptyIQ = new IQ();
            emptyIQ.setType("result");
            emptyIQ.setId(iqQueryRegister.getId());
            session.sendStanza(emptyIQ);

            System.out.println("New user registered!");
            System.out.println("username = " + user.getUsername());
            System.out.println("password = " + user.getPassword());
            System.out.println("apiKey = " + user.getApiKey());
        }
        else
        {
            //TODO: username is already exist. close this session.
            System.out.println("register failed");
        }
    }

    private void handleAck(IQAck iqAck)
    {
        //put the stanzaId of the ack into NotificationService and MessageService to confirm
        NotificationService notificationService = (NotificationService)ServiceManager.getInstance().getService("notificationService");
        notificationService.confirmNotification(iqAck.getId());

        MessageService messageService = (MessageService)ServiceManager.getInstance().getService("messageService");
        messageService.confirmMessage(iqAck.getId());
    }

    private void handlePing(Session session, IQPing iqPing)
    {
        IQ iq = new IQ();
        iq.setFrom(iqPing.getTo());
        iq.setTo(iqPing.getFrom());
        iq.setId(iqPing.getId());
        iq.setType("result");

        session.sendStanza(iq);
    }

    private void handleSetAlias(Session session, IQAlias iqAlias)
    {
        UserService userService = (UserService)ServiceManager.getInstance().getService("userService");
        boolean setAliasResult = userService.setUserAlias(session.getUser(), iqAlias.getAlias());

        IQAck iqAck = new IQAck();
        iqAck.setIsSuccess(setAliasResult);
        iqAck.setType("set");
        iqAck.setId(iqAlias.getId());
        iqAck.setFrom(iqAck.getTo());
        iqAck.setTo(iqAlias.getFrom());
        session.sendStanza(iqAck);
    }

    private String getRandomString()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
