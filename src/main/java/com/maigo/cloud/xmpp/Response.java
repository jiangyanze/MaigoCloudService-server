package com.maigo.cloud.xmpp;

public class Response extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    private String xmlns;
    private String text;

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        if(nodeName.equals("response"))
            this.text = text;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<response");
        stringBuilder.append(" xmlns='" + xmlns + "'>");
        stringBuilder.append(text);
        stringBuilder.append("</response>");

        return stringBuilder.toString();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
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
