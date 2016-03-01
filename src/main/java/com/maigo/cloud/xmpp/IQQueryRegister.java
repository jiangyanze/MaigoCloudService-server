package com.maigo.cloud.xmpp;

public class IQQueryRegister extends IQQuery
{
    private String username;
    private String password;
    private String apiKey;

    public IQQueryRegister()
    {
        super("jabber:iq:register");
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);
        if(nodeName.equals("username"))
            username = text;
        else if(nodeName.equals("password"))
            password = text;
        else if(nodeName.equals("apiKey"))
            apiKey = text;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    @Override
    protected String getQueryChildXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<username/>");
        stringBuilder.append("<password/>");
        stringBuilder.append("<apiKey/>");
        stringBuilder.append("<x xmlns='jabber:x:data' type='form'>");
        stringBuilder.append("<title>Maigo Cloud Service Registration</title>");
        stringBuilder.append("<instructions>Please provide the following information</instructions>");
        stringBuilder.append("<field var='FORM_TYPE' type='hidden'><value>jabber:iq:register</value></field>");
        stringBuilder.append("<field label='Username' var='username' type='text-single'><required/></field>");
        stringBuilder.append("<field label='Password' var='password' type='text-private'><required/></field>");
        stringBuilder.append("<field label='ApiKey' var='apiKey' type='text-private'><required/></field>");
        stringBuilder.append("</x>");

        return stringBuilder.toString();
    }
}
