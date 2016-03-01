package com.maigo.cloud.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ServiceManager implements ApplicationContextAware
{
    private ApplicationContext applicationContext = null;
    private static ServiceManager instance = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public void init()
    {
        ServiceManager.instance = this;
    }

    public static ServiceManager getInstance()
    {
        return instance;
    }

    public Object getService(String serviceName)
    {
        return applicationContext.getBean(serviceName);
    }
}
