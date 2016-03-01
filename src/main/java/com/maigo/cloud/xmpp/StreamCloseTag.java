package com.maigo.cloud.xmpp;

public class StreamCloseTag extends Stanza
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
        return "</stream:stream>";
    }
}
