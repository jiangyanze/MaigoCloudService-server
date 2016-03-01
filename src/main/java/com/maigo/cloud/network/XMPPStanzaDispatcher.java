package com.maigo.cloud.network;

import com.maigo.cloud.handler.*;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.SessionService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashSet;
import java.util.Set;

public class XMPPStanzaDispatcher extends ChannelInboundHandlerAdapter
{
    private Set<StanzaHandler> registerStanzaHandlerSet = new HashSet<StanzaHandler>();

    public XMPPStanzaDispatcher()
    {
        registerStanzaHandlerSet.add(new AuthHandler());
        registerStanzaHandlerSet.add(new IQHandler());
        registerStanzaHandlerSet.add(new ResponseHandler());
        registerStanzaHandlerSet.add(new StartTLSHandler());
        registerStanzaHandlerSet.add(new StreamOpenTagHandler());
        registerStanzaHandlerSet.add(new StreamCloseTagHandler());
        //add custom stanza handler here to handle more XMPP stanzas
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        //get the SessionManageService and create an new session
        SessionService sessionManageService = (SessionService) ServiceManager.getInstance().getService("sessionService");
        Session session = new Session(ctx.channel());
        session.setState(Session.STATE_IDLE);
        sessionManageService.addSession(session.getSessionKey(), session);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String stanza = msg.toString();
        SessionService sessionManageService = (SessionService) ServiceManager.getInstance().getService("sessionService");
        Session session = sessionManageService.getSession(ctx.channel().hashCode());

        StanzaHandler stanzaHandler = getStanzaHandler(stanza);
        if(stanzaHandler != null)
        {
            stanzaHandler.handle(stanza, session);
        }
        else
        {
            //TODO: no register stanza handler is found!
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
    }

    private StanzaHandler getStanzaHandler(String stanza)
    {
        for(StanzaHandler stanzaHandler : registerStanzaHandlerSet)
        {
            if(stanza.startsWith(stanzaHandler.getStanzaStartsWith()))
                return stanzaHandler;
        }

        return null;
    }
}
