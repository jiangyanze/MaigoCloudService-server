package com.maigo.cloud.network;

import com.maigo.cloud.model.User;
import com.maigo.cloud.xmpp.Stanza;
import com.maigo.cloud.xmpp.StreamCloseTag;
import io.netty.channel.Channel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * wrap a netty channel as a session. A session has its own state and methods to send packets.
 */
public class Session
{
    private Channel channel;

    private User user;

    /**
     * state that just build a channel
     */
    public final static int STATE_IDLE = 0;

    /**
     * state that after receive the first <stream:stream> tag
     */
    public final static int STATE_CONNECTED = 1;

    /**
     * state that after receive the <starttls/> tag
     */
    public final static int STATE_STARTED_TLS = 2;

    /**
     * state that after receive the second <stream:stream> tag
     */
    public final static int STATE_STARTED_SASL = 3;

    /**
     * state that after receive the <Auth/> tag contains the mechanism(DIGEST-MD5 only)
     */
    public final static int STATE_SELECTED_MECHANISM = 4;

    /**
     * state that after receive the first <response>...</response> tag
     */
    public final static int STATE_RESPONSED_AUTH = 5;

    /**
     * state that after completely authenticated
     */
    public final static int STATE_AUTHENTICATED = 6;

    /**
     * state that start to bind the resource
     */
    public final static int STATE_STARTED_BIND = 7;

    /**
     * state that after bind the resource
     */
    public final static int STATE_RESOURCE_BOUND = 8;

    /**
     * state that after receive the <iq><session/></iq>
     */
    public final static int STATE_SESSION_REQUESTED = 9;

    /**
     * state that this session is able to exchange common xmpp stanza between client and server
     */
    public final static int STATE_ACTIVE = 10;

    /**
     * state that this session is closed
     */
    public final static int STATE_CLOSED = 11;

    private int state = STATE_IDLE;

    public Session(Channel channel)
    {
        this.channel = channel;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public boolean isIdle()
    {
        return state == STATE_IDLE;
    }

    public boolean isConnected()
    {
        return state == STATE_CONNECTED;
    }

    public boolean isStartedTLS()
    {
        return state == STATE_STARTED_TLS;
    }

    public boolean isStartedSASL()
    {
        return state == STATE_STARTED_SASL;
    }

    public boolean isSelectedMechanism()
    {
        return state == STATE_SELECTED_MECHANISM;
    }

    public boolean isResponsedAuth()
    {
        return state == STATE_RESPONSED_AUTH;
    }

    public boolean isAuthenticated()
    {
        return state == STATE_AUTHENTICATED;
    }

    public boolean isStartedBind()
    {
        return state == STATE_STARTED_BIND;
    }

    public boolean isResourceBound()
    {
        return state == STATE_RESOURCE_BOUND;
    }

    public boolean isSessionRequested()
    {
        return state == STATE_SESSION_REQUESTED;
    }

    public boolean isActive()
    {
        return state == STATE_ACTIVE;
    }

    public boolean isClosed()
    {
        return state == STATE_CLOSED;
    }

    /**
     * send a message to the client which this session is from
     */
    public void sendStanza(Stanza stanza)
    {
        channel.write(stanza);
    }

    /**
     * get the key of this session in the SessionManageService
     * @return the key
     */
    public int getSessionKey()
    {
        return channel.hashCode();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void close()
    {
        sendStanza(new StreamCloseTag());
        channel.close();
    }

    public void startTLS(SSLContext sslContext)
    {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        sslEngine.setNeedClientAuth(true);

        SslHandler sslHandler = new SslHandler(sslEngine, true);
        channel.pipeline().addFirst(sslHandler);
    }
}
