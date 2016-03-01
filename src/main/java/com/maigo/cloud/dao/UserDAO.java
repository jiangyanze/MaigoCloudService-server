package com.maigo.cloud.dao;

import com.maigo.cloud.model.User;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserDAO extends BasicDAO
{
    public void addUser(User user)
    {
        Session session = getCurrentSession();
        session.save(user);
    }

    public User getUserByUsername(String username)
    {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User as user where user.username = '" + username + "'");

        User user = null;

        try
        {
            user = (User)query.uniqueResult();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public User getUserByAlias(String alias)
    {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User as user where user.alias = '" + alias + "'");

        User user = null;

        try
        {
            user = (User)query.uniqueResult();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public void updateUser(User user)
    {
        Session session = getCurrentSession();
        session.update(user);
    }
}
