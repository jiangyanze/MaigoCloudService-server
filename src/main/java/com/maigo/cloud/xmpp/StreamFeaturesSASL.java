package com.maigo.cloud.xmpp;

public class StreamFeaturesSASL extends Stanza
{
    public final static String DEFAULT_MECHANISM = "DIGEST-MD5";    //support DIGEST-MD5 only

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

        stringBuilder.append("<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
        stringBuilder.append("<mechanism>" + DEFAULT_MECHANISM + "</mechanism>");
        stringBuilder.append("</mechanisms>");

        stringBuilder.append("</stream:features>");

        return stringBuilder.toString();
    }
}
