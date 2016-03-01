package com.maigo.cloud.handler;

import com.maigo.cloud.network.Session;

public interface StanzaHandler
{
    public void handle(String stanza, Session session);

    public String getStanzaStartsWith();
}
