package com.maigo.cloud.handler;

import com.maigo.cloud.authentication.AuthenticationManager;
import com.maigo.cloud.network.Session;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.SessionService;
import com.maigo.cloud.xmpp.Challenge;
import com.maigo.cloud.xmpp.Response;
import com.maigo.cloud.xmpp.Success;
import org.bouncycastle.util.encoders.Base64;

public class ResponseHandler implements StanzaHandler
{
    private AuthenticationManager authenticationManager = new AuthenticationManager();

    public void handle(String stanza, Session session)
    {
        if(stanza.startsWith("<response") && session.isSelectedMechanism() && !session.isResponsedAuth())
        {
            //to make sure if the <response> tag has been received or not, need to check session.isResponsedAuth()
            Response response = new Response();
            response.parse(stanza);
            if(!response.getXmlns().equals(Response.DEFAULT_XMLNS))
            {
                //TODO: receive invalidate <response/> tag
            }

            handleResponseFromSession(session, response);
        }
        else if(stanza.startsWith("<response") && session.isResponsedAuth())
        {
            completeConnectionAuthentication(session);
        }
    }

    public String getStanzaStartsWith()
    {
        return "<response";
    }

    private void handleResponseFromSession(Session session, Response response)
    {
        String responseText = response.getText();
        String content = new String(Base64.decode(responseText));
        boolean authResult = authenticationManager.authenticate(content);

        if(authResult)
        {
            Challenge challenge = new Challenge();
            challenge.setXmlns(Challenge.DEFAULT_XMLNS);

            String base64Content = new String(Base64.encode(("rspauth=" + authenticationManager.getRspauthValue()).getBytes()));

            challenge.setContent(base64Content);

            session.setState(Session.STATE_RESPONSED_AUTH);
            session.setUser(authenticationManager.getAuthenticatedUser());
            SessionService sessionManageService = (SessionService) ServiceManager.getInstance().getService("sessionService");
            sessionManageService.bindSessionWithUser(session, authenticationManager.getAuthenticatedUser());

            session.sendStanza(challenge);
        }
        else
        {
            Response failResponse = new Response();
            failResponse.setXmlns(Response.DEFAULT_XMLNS);
            failResponse.setText("");
            session.sendStanza(failResponse);
            //TODO: Close the session
        }
    }

    private void completeConnectionAuthentication(Session session)
    {
        session.setState(Session.STATE_AUTHENTICATED);
        Success success = new Success();
        success.setXmlns(Success.DEFAULT_XMLNS);
        session.sendStanza(success);
    }
}
