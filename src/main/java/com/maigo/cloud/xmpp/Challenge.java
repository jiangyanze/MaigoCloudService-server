package com.maigo.cloud.xmpp;

public class Challenge extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    private String xmlns;
    private String content;

    public String getXmlns()
    {
        return xmlns;
    }

    public String getContent()
    {
        return content;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        if(nodeName.equals("challenge"))
            content = text;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(nodeName.equals("challenge") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<challenge");
        stringBuilder.append(" xmlns='" + xmlns + "'>");
        stringBuilder.append(content);
        stringBuilder.append("</challenge>");

        return stringBuilder.toString();
    }
}
