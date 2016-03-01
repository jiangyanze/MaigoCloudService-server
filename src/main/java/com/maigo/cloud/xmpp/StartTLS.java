package com.maigo.cloud.xmpp;

public class StartTLS extends Stanza
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-tls";

    private String xmlns;

    private boolean required;

    /**
     * a <required/> tag will be insert into the <starttls></starttls> if required is set.
     * @param required
     */
    public StartTLS(boolean required)
    {
        this.required = required;
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        if(nodeName.equals("required"))
            required = true;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(nodeName.equals("starttls") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<starttls");
        stringBuilder.append(" xmlns='" + xmlns + "'>");

        if(required)
            stringBuilder.append("<required/>");

        stringBuilder.append("</starttls>");

        return null;
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
