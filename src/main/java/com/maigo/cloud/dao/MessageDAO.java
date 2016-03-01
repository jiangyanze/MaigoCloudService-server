package com.maigo.cloud.dao;

import com.maigo.cloud.model.Message;
import com.maigo.cloud.model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class MessageDAO extends BasicDAO
{
    /**
     * save a message into the database.
     * @param message
     * @return the message ID
     */
    public String addMessage(Message message)
    {
        Session session = getCurrentSession();
        return (session.save(message)).toString();
    }

    public Message getMessage(String messageId)
    {
        Session session = getCurrentSession();
        return (Message)session.get(Message.class, Integer.parseInt(messageId));
    }

    public void updateMessage(Message message)
    {
        Session session = getCurrentSession();
        session.update(message);
    }

    public List<Message> getOfflineMessageList(User user)
    {
        Session session = getCurrentSession();
        Query query = session.createQuery("from Message as message where message.username = '"
                + user.getUsername() + "' and message.isConfirmed = false");
        return query.list();
    }
}
