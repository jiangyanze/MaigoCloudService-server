package com.maigo.cloud.xmpp;

public class Success extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    private String xmlns;

    public String getXmlns()
    {
        return xmlns;
    }

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
        if(nodeName.equals("success") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<success");
        stringBuilder.append(" xmlns='" + xmlns + "'/>");

        return stringBuilder.toString();
    }
}
