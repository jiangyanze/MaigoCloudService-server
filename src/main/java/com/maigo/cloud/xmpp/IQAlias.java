package com.maigo.cloud.xmpp;

public class IQAlias extends IQ
{
    public final static String DEFAULT_XMLNS = "maigo.cloud.service:alias";

    private String xmlns;
    private String alias;

    public IQAlias()
    {
        super("alias", DEFAULT_XMLNS);
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);
        if(nodeName.equals("alias"))
            alias = text;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);
        if(nodeName.equals("alias") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    protected String getIQChildXmlString()
    {
        return super.getIQChildXmlString();
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
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
