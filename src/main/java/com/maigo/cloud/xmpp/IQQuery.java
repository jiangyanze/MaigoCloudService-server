package com.maigo.cloud.xmpp;

public class IQQuery extends IQ
{
    public IQQuery()
    {
        this(null);
    }

    public IQQuery(String xmlns)
    {
        super("query", xmlns);
    }

    private String xmlns;

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);
        if(nodeName.equals("query") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    protected final String getIQChildXmlString()
    {
        return getQueryChildXmlString();
    }

    protected String getQueryChildXmlString()
    {
        return "";
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }
}
