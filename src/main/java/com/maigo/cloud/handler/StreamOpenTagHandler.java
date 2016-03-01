package com.maigo.cloud.handler;

import com.maigo.cloud.network.Session;
import com.maigo.cloud.xmpp.StreamFeaturesBind;
import com.maigo.cloud.xmpp.StreamFeaturesSASL;
import com.maigo.cloud.xmpp.StreamFeaturesTLS;
import com.maigo.cloud.xmpp.StreamOpenTag;

public class StreamOpenTagHandler implements StanzaHandler
{
    private final static String SERVER_NAME = "MaigoCloudService";

    public void handle(String stanza, Session session)
    {
        if(stanza.startsWith("<stream:stream") && session.isIdle())
        {
            connectWithSession(session);
        }
        else if(stanza.startsWith("<stream:stream") && session.isStartedTLS())
        {
            startSASLWithSession(session);
        }
        else if(stanza.startsWith("<stream:stream") && session.isAuthenticated())
        {
            startBindWithSession(session);
        }
    }

    public String getStanzaStartsWith()
    {
        return "<stream:stream";
    }

    private void connectWithSession(Session session)
    {
        //return the <stream:stream> tag to the client
        StreamOpenTag streamOpenTag = new StreamOpenTag();
        streamOpenTag.setFrom(SERVER_NAME);
        streamOpenTag.setXmlns(StreamOpenTag.DEFAULT_XMLNS);
        streamOpenTag.setXmlnsStream(StreamOpenTag.DEFAULT_XMLNS_STREAM);
        streamOpenTag.setVersion(StreamOpenTag.DEFAULT_VERSION);
        streamOpenTag.setXmlLang(StreamOpenTag.DEFAULT_XML_LANG);

        //return the <stream:features/> tag to the client
        StreamFeaturesTLS streamFeaturesTLS = new StreamFeaturesTLS();

        session.setState(Session.STATE_CONNECTED);

        session.sendStanza(streamOpenTag);
        session.sendStanza(streamFeaturesTLS);
    }

    private void startSASLWithSession(Session session)
    {
        //return the <stream:stream> tag to the client
        StreamOpenTag streamOpenTag = new StreamOpenTag();
        streamOpenTag.setFrom(SERVER_NAME);
        streamOpenTag.setXmlns(StreamOpenTag.DEFAULT_XMLNS);
        streamOpenTag.setXmlnsStream(StreamOpenTag.DEFAULT_XMLNS_STREAM);
        streamOpenTag.setVersion(StreamOpenTag.DEFAULT_VERSION);
        streamOpenTag.setXmlLang(StreamOpenTag.DEFAULT_XML_LANG);

        //return the <stream:features/> tag to the client
        StreamFeaturesSASL streamFeaturesSASL = new StreamFeaturesSASL();

        session.setState(Session.STATE_STARTED_SASL);

        session.sendStanza(streamOpenTag);
        session.sendStanza(streamFeaturesSASL);
    }

    private void startBindWithSession(Session session)
    {
        StreamOpenTag streamOpenTag = new StreamOpenTag();
        streamOpenTag.setFrom(SERVER_NAME);
        streamOpenTag.setXmlns(StreamOpenTag.DEFAULT_XMLNS);
        streamOpenTag.setXmlnsStream(StreamOpenTag.DEFAULT_XMLNS_STREAM);
        streamOpenTag.setVersion(StreamOpenTag.DEFAULT_VERSION);
        streamOpenTag.setXmlLang(StreamOpenTag.DEFAULT_XML_LANG);

        StreamFeaturesBind streamFeaturesBind = new StreamFeaturesBind();

        session.setState(Session.STATE_STARTED_BIND);

        session.sendStanza(streamOpenTag);
        session.sendStanza(streamFeaturesBind);
    }
}
