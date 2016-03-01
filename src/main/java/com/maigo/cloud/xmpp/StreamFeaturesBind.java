package com.maigo.cloud.xmpp;

public class StreamFeaturesBind extends Stanza
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
        stringBuilder.append("<compression xmlns=\'http://jabber.org/features/compress\'>");
        stringBuilder.append("<method>zlib</method>");
        stringBuilder.append("</compression>");
        stringBuilder.append("<bind xmlns=\'urn:ietf:params:xml:ns:xmpp-bind\'/>");
        stringBuilder.append("<session xmlns=\'urn:ietf:params:xml:ns:xmpp-session\'/>");

        stringBuilder.append("</stream:features>");

        return stringBuilder.toString();
    }
}
