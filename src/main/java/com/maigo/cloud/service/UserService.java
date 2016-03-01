package com.maigo.cloud.service;

import com.maigo.cloud.dao.UserDAO;
import com.maigo.cloud.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private UserDAO userDAO;

    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * register a user into the databases. return false if the username is already exist and register fail.
     * @param user the user to register
     * @return if the username is already exist
     */
    public boolean registerUser(User user)
    {
        if(userDAO.getUserByUsername(user.getUsername()) != null)
            return false;

        userDAO.addUser(user);
        return true;
    }

    /**
     * get a user by its username. return null if a user with the same username is not found.
     * @param username the username
     * @return the user
     */
    public User getUserByUsername(String username)
    {
        return userDAO.getUserByUsername(username);
    }

    /**
     * set alias of a user.
     * @param user
     * @param alias
     * @return false if a user has already has the very alias.
     */
    public boolean setUserAlias(User user, String alias)
    {
        if(userDAO.getUserByAlias(alias) != null)
            return false;

        user.setAlias(alias);
        userDAO.updateUser(user);
        return true;
    }

    /**
     * get a user by its alias. return null if a user with the same alias is not found.
     * @param alias the alias
     * @return the user
     */
    public User getUserByAlias(String alias)
    {
        return userDAO.getUserByAlias(alias);
    }
}
