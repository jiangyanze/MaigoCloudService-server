package com.maigo.cloud.xmpp;

public class Proceed extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-tls";

    private String xmlns;

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {

    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(nodeName.equals("proceed") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<proceed");
        stringBuilder.append(" xmlns='" + xmlns + "'/>");

        return stringBuilder.toString();
    }
}
