package com.maigo.cloud.xmpp;

public class IQPing extends IQ
{
    public final static String DEFAULT_XMLNS = "urn:xmpp:ping";

    private String xmlns;

    public IQPing()
    {
        super("ping", DEFAULT_XMLNS);
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

        if(nodeName.equals("ping") && attr.equals("xmlns"))
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
