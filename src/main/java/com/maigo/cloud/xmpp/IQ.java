package com.maigo.cloud.xmpp;

public class IQ extends Stanza
{
    private String childNodeName;
    private String childXmlns;

    public IQ()
    {

    }

    public IQ(String childNodeName, String childXmlns)
    {
        this.childNodeName = childNodeName;
        this.childXmlns = childXmlns;
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {

    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        if(attr.equals("xmlns"))
        {
            childNodeName = nodeName;
            childXmlns = value;
        }
    }

    @Override
    public final String toXmlString()
    {
        if(type == null)
            type = "get";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<iq type='");
        stringBuilder.append(type);
        stringBuilder.append("' id='");
        stringBuilder.append(id);

        if(from != null)
        {
            stringBuilder.append("' from='");
            stringBuilder.append(from);
        }

        if(to != null)
        {
            stringBuilder.append("' to='");
            stringBuilder.append(to);
        }

        stringBuilder.append("'>");

        if(childNodeName != null)
        {
            stringBuilder.append("<");
            stringBuilder.append(childNodeName);
            if(childXmlns != null)
            {
                stringBuilder.append(" xmlns='");
                stringBuilder.append(childXmlns);
                stringBuilder.append("'");
            }
            stringBuilder.append(">");
        }

        stringBuilder.append(getIQChildXmlString());

        if(childNodeName != null)
        {
            stringBuilder.append("</");
            stringBuilder.append(childNodeName);
            stringBuilder.append(">");
        }

        stringBuilder.append("</iq>");

        return stringBuilder.toString();
    }

    /**
     * child should override this method to tell IQ what to insert into the <ChildNodeName>...</ChildNodeName>
     * @return the content.
     */
    protected String getIQChildXmlString()
    {
        return "";
    }

    public String getChildXmlns()
    {
        return childXmlns;
    }

    public void setChildXmlns(String childXmlns)
    {
        this.childXmlns = childXmlns;
    }

    public String getChildNodeName()
    {
        return childNodeName;
    }

    public void setChildNodeName(String childNodeName)
    {
        this.childNodeName = childNodeName;
    }
}