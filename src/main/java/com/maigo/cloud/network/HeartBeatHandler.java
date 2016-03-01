package com.maigo.cloud.network;

import com.maigo.cloud.model.User;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.SessionService;
import com.maigo.cloud.xmpp.StreamCloseTag;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class HeartBeatHandler extends IdleStateHandler
{
    public HeartBeatHandler()
    {
        //client auto ping server every 60s, close session if no ping was receive in 120s.
        super(120, 0, 0);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception
    {
        if(evt.state().toString().equals("READER_IDLE"))
        {
            SessionService sessionService = (SessionService)ServiceManager.getInstance().getService("sessionService");
            int key = ctx.channel().hashCode();
            Session session = sessionService.getSession(key);

            sessionService.removeSession(key);

            session.setState(Session.STATE_CLOSED);
            session.close();
        }
        else
        {
            throw new IllegalStateException(evt.state().toString() + " is triggered but should be disabled.");
        }
    }
}
