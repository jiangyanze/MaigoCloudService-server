package com.maigo.cloud.service;

import com.maigo.cloud.model.User;
import com.maigo.cloud.network.Session;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService
{
    private Map<Integer, Session> currentSessions;

    private Map<String, Session> userSessions;

    public SessionService()
    {
        currentSessions = new ConcurrentHashMap<Integer, Session>();
        userSessions = new ConcurrentHashMap<String, Session>();
    }

    /**
     * add a session to the SessionManageService
     * @param key hashcode of the channel in the session
     * @param session the session
     */
    public void addSession(int key, Session session)
    {
        if(currentSessions.containsKey(key))
            throw new IllegalArgumentException("key of session is already existed.");

        currentSessions.put(key, session);
    }

    /**
     * bind a session with a user. getSession(User) is available after the bind.
     * @param session
     * @param user
     */
    public void bindSessionWithUser(Session session, User user)
    {
        userSessions.put(user.getUsername(), session);
    }

    /**
     * get the session maps with the key
     * @param key hashcode of the channel
     * @return the session. null is return if the session is not found.
     */
    public Session getSession(int key)
    {
        return currentSessions.get(key);
    }

    /**
     * get the session that is already bind with the user
     * @param user the user
     * @return the session. null is return if session bind with the user is not found.
     */
    public Session getSessionBindWithUser(User user)
    {
        return userSessions.get(user.getUsername());
    }

    /**
     * remove the session maps with the key, also unbind with the user if the session have bound with a user.
     * @param key hashcode of the channel
     */
    public void removeSession(int key)
    {
        Session session = currentSessions.remove(key);

        if(session != null)
        {
            User user = session.getUser();
            if(user != null)
            {
                //a user has bound with this session
                userSessions.remove(user.getUsername());
            }
        }
    }

    /**
     * remove the session, also unbind with the user if the session have bound with a user.
     * @param session the session
     */
    public void removeSession(Session session)
    {
        removeSession(session.getSessionKey());
    }
}
