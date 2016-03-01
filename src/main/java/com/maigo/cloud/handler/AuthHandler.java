package com.maigo.cloud.handler;

import com.maigo.cloud.network.Session;
import com.maigo.cloud.xmpp.Auth;
import com.maigo.cloud.xmpp.Challenge;
import org.bouncycastle.util.encoders.Base64;

import java.util.UUID;

public class AuthHandler implements StanzaHandler
{
    private final static String SERVER_NAME = "MaigoCloudService";

    public void handle(String stanza, Session session)
    {
        if(stanza.startsWith("<auth") && session.isStartedSASL())
        {
            Auth auth = new Auth();
            auth.parse(stanza);
            if(!auth.getXmlns().equals(Auth.DEFAULT_XMLNS) || !auth.getMechanism().equals(Auth.DEFAULT_MECHANISM))
            {
                //TODO: receive <auth/> with incorrect xmlns or mechanism which is not DIGEST-MD5(only support mechanism)
            }

            startAuthWithSession(session);
        }
    }

    public String getStanzaStartsWith()
    {
        return "<auth";
    }

    private void startAuthWithSession(Session session)
    {
        Challenge challenge = new Challenge();
        challenge.setXmlns(Challenge.DEFAULT_XMLNS);

        StringBuilder content = new StringBuilder();
        content.append("realm=\"");
        content.append(SERVER_NAME);
        content.append("\",nonce=\"");
        String nonce = getRandomString();
        content.append(nonce);
        content.append("\",qop=\"auth\",charset=utf-8,algorithm=md5-sess");

        String base64Challenge = new String(Base64.encode(content.toString().getBytes()));
        challenge.setContent(base64Challenge);

        session.setState(Session.STATE_SELECTED_MECHANISM);

        session.sendStanza(challenge);
    }

    private String getRandomString()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
