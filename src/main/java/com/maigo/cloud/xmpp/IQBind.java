package com.maigo.cloud.xmpp;

public class IQBind extends IQ
{
    public final static String DEFAULT_XMLNS = "urn:ietf:params:xml:ns:xmpp-bind";

    private String xmlns;
    private String resource;
    private String jid;

    public IQBind()
    {
        super("bind", DEFAULT_XMLNS);
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);

        if(nodeName.equals("resource"))
            resource = text;
        else if(nodeName.equals("jid"))
            jid = text;
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);

        if(nodeName.equals("bind") && attr.equals("xmlns"))
            xmlns = value;
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public String getResource()
    {
        return resource;
    }

    public String getJid()
    {
        return jid;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public void setJid(String jid)
    {
        this.jid = jid;
    }

    @Override
    protected final String getIQChildXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(resource != null)
        {
            stringBuilder.append("<resource>);");
            stringBuilder.append(resource);
            stringBuilder.append("</resource>");
        }

        if(jid != null)
        {
            stringBuilder.append("<jid>");
            stringBuilder.append(jid);
            stringBuilder.append("</jid>");
        }

        return stringBuilder.toString();
    }
}
