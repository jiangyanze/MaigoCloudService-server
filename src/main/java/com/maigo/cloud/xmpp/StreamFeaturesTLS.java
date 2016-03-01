package com.maigo.cloud.xmpp;

public class StreamFeaturesTLS extends Stanza
{
    @Override
    protected void onParseNodeText(String nodeName, String text)
    {

    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {

    }

    @Override
    public String toXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<stream:features>");

        stringBuilder.append("<starttls xmlns=\'" + StartTLS.DEFAULT_XMLNS + "\'>");
        stringBuilder.append("<required/>");
        stringBuilder.append("</starttls>");

        stringBuilder.append("</stream:features>");

        return stringBuilder.toString();
    }
}
