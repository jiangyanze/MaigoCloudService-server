package com.maigo.cloud.xmpp;

public class IQSession extends IQ
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-session";

    private String xmlns;

    public IQSession()
    {
        super("session", DEFAULT_XMLNS);
    }


    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);

        if(nodeName.equals("session") && attr.equals("xmlns"))
            xmlns = value;
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    @Override
    protected final String getIQChildXmlString()
    {
        return "";
    }
}
