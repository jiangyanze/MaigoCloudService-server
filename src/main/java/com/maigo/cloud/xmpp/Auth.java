package com.maigo.cloud.xmpp;

public class Auth extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";
    public final static String DEFAULT_MECHANISM = "DIGEST-MD5";

    private String xmlns;
    private String mechanism;

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {

    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(attr.equals("xmlns"))
            xmlns = value;
        else if(attr.equals("mechanism"))
            mechanism = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<auth xmlns='");
        stringBuilder.append(xmlns);
        stringBuilder.append("' mechanism='");
        stringBuilder.append(mechanism);
        stringBuilder.append("'>");
        stringBuilder.append("=");
        stringBuilder.append("</auth>");

        return stringBuilder.toString();
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public String getMechanism()
    {
        return mechanism;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    public void setMechanism(String mechanism)
    {
        this.mechanism = mechanism;
    }
}
