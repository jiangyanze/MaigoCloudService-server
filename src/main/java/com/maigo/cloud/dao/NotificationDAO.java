package com.maigo.cloud.dao;

import com.maigo.cloud.model.Notification;
import com.maigo.cloud.model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class NotificationDAO extends BasicDAO
{
    /**
     * save a notification into the database.
     * @param notification
     * @return the notification ID
     */
    public String addNotification(Notification notification)
    {
        Session session = getCurrentSession();
        return (session.save(notification)).toString();
    }

    public Notification getNotification(String notificationId)
    {
        Session session = getCurrentSession();
        return (Notification)session.get(Notification.class, Integer.parseInt(notificationId));
    }

    public void updateNotification(Notification notification)
    {
        Session session = getCurrentSession();
        session.update(notification);
    }

    public List<Notification> getOfflineNotificationList(User user)
    {
        Session session = getCurrentSession();
        Query query = session.createQuery("from Notification as notification where notification.username = '"
                + user.getUsername() + "' and notification.isConfirmed = false");
        return query.list();
    }
}
