package com.maigo.cloud.handler;

import com.maigo.cloud.network.Session;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.SessionService;

public class StreamCloseTagHandler implements StanzaHandler
{
    public void handle(String stanza, Session session)
    {
        SessionService sessionService = (SessionService)ServiceManager.getInstance().getService("sessionService");
        sessionService.removeSession(session);

        session.setState(Session.STATE_CLOSED);
        session.close();
    }

    public String getStanzaStartsWith()
    {
        return "</stream:stream";
    }
}
