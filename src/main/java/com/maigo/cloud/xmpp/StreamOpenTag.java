package com.maigo.cloud.xmpp;

public class StreamOpenTag extends Stanza
{
    public final static String DEFAULT_XMLNS = "jabber:client";
    public final static String DEFAULT_XMLNS_STREAM = "http://etherx.jabber.org/streams";
    public final static String DEFAULT_VERSION = "1.0";
    public final static String DEFAULT_XML_LANG = "en";

    String xmlns;
    String from;
    String to;
    String id;
    String xmlnsStream;
    String version;
    String xmlLang;

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

    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<stream:stream");

        if(xmlns != null)
            stringBuilder.append(" xmlns=\'").append(xmlns).append("\'");

        if(xmlnsStream != null)
            stringBuilder.append(" xmlns:stream=\'").append(xmlnsStream).append("\'");

        if(id != null)
            stringBuilder.append(" id=\'").append(id).append("\'");

        if(from != null)
            stringBuilder.append(" from=\'").append(from).append("\'");

        if(to != null)
            stringBuilder.append(" to=\'").append(to).append("\'");

        if(version != null)
            stringBuilder.append(" version=\'").append(version).append("\'");

        if(xmlLang != null)
            stringBuilder.append(" xml:lang=\'").append(xmlLang).append("\'");

        stringBuilder.append(">");

        return stringBuilder.toString();
    }

    public String getXmlnsStream()
    {
        return xmlnsStream;
    }

    public void setXmlnsStream(String xmlnsStream)
    {
        this.xmlnsStream = xmlnsStream;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getXmlLang()
    {
        return xmlLang;
    }

    public void setXmlLang(String xmlLang)
    {
        this.xmlLang = xmlLang;
    }
}
