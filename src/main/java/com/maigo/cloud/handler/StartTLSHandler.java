package com.maigo.cloud.handler;

import com.maigo.cloud.network.Session;
import com.maigo.cloud.service.SSLConfigService;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.xmpp.Proceed;
import com.maigo.cloud.xmpp.StartTLS;
import io.netty.channel.Channel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class StartTLSHandler implements StanzaHandler
{
    public void handle(String stanza, Session session)
    {
        if(stanza.startsWith("<starttls") && session.isConnected())
        {
            StartTLS startTLS = new StartTLS(false);
            startTLS.parse(stanza);
            if(!startTLS.getXmlns().equals(StartTLS.DEFAULT_XMLNS))
            {
                //TODO: receive invalidate <starttls/> tag
            }

            startTLSWithSession(session);
        }
    }

    public String getStanzaStartsWith()
    {
        return "<starttls";
    }

    private void startTLSWithSession(Session session)
    {
        try
        {
            SSLConfigService sslConfigService = (SSLConfigService) ServiceManager.getInstance().getService("sslConfigService");
            KeyStore keyStoreServerKey = sslConfigService.getKeyStoreServerKey();
            KeyStore keyStoreServerTrust = sslConfigService.getKeyStoreServerTrust();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");

            keyManagerFactory.init(keyStoreServerKey, sslConfigService.getKeyStoreServerKeyPassword().toCharArray());
            trustManagerFactory.init(keyStoreServerTrust);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            session.startTLS(sslContext);
            session.setState(Session.STATE_STARTED_TLS);

            Proceed proceed = new Proceed();
            proceed.setXmlns(Proceed.DEFAULT_XMLNS);
            session.sendStanza(proceed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
