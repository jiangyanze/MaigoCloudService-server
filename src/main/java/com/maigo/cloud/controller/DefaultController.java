package com.maigo.cloud.controller;

import com.maigo.cloud.model.Message;
import com.maigo.cloud.model.Notification;
import com.maigo.cloud.model.User;
import com.maigo.cloud.network.Session;
import com.maigo.cloud.service.*;
import com.maigo.cloud.xmpp.IQNotification;
import com.maigo.cloud.xmpp.IQQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class DefaultController
{
    @RequestMapping(value = "/")
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/push")
    public ModelAndView index(String target, String title, String content, String pushType, String targetType)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        String username = "";
        if(targetType.equals("alias"))
        {
            UserService userService = (UserService) ServiceManager.getInstance().getService("userService");
            User targetUser = userService.getUserByAlias(target);
            if(targetUser == null)
                return modelAndView;

            username = targetUser.getUsername();
        }
        else
            username = target;

        if(pushType.equals("notification"))
        {
            Notification notification = new Notification();
            notification.setUsername(username);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setConfirmed(false);

            NotificationService notificationService = (NotificationService) ServiceManager.getInstance().getService("notificationService");
            notificationService.sendNotification(notification);
        }
        else if(pushType.equals("message"))
        {
            Message message = new Message();
            message.setUsername(username);
            message.setTitle(title);
            message.setContent(content);
            message.setConfirmed(false);

            MessageService messageService = (MessageService) ServiceManager.getInstance().getService("messageService");
            messageService.sendMessage(message);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/test")
    public ModelAndView test()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }
}
