package com.maigo.cloud.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@Service
public class SSLConfigService implements ApplicationContextAware
{
    private ApplicationContext applicationContext = null;
    private KeyStore keyStoreServerKey = null;
    private KeyStore keyStoreServerTrust = null;

    public void loadSSLConfig()
    {
        try
        {
            keyStoreServerKey = KeyStore.getInstance("BKS");
            keyStoreServerTrust = KeyStore.getInstance("BKS");

            ServletContext servletContext = (ServletContext) applicationContext.getBean(WebApplicationContext.SERVLET_CONTEXT_BEAN_NAME);
            String servletPath = servletContext.getRealPath(".");
            String keystorePath = servletPath + File.separator + "WEB-INF" + File.separator + "classes"
                    + File.separator + "security-keystore";

            keyStoreServerKey.load(new FileInputStream(keystorePath + File.separator + "server_key.bks"),
                    getKeyStoreServerKeyPassword().toCharArray());
            keyStoreServerTrust.load(new FileInputStream(keystorePath + File.separator + "server_trust.bks"),
                    getKeyStoreServerTrustPassword().toCharArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public KeyStore getKeyStoreServerKey()
    {
        return keyStoreServerKey;
    }

    public KeyStore getKeyStoreServerTrust()
    {
        return keyStoreServerTrust;
    }

    public String getKeyStoreServerKeyPassword()
    {
        return "5201314";
    }

    public String getKeyStoreServerTrustPassword()
    {
        return "5201314";
    }
}
