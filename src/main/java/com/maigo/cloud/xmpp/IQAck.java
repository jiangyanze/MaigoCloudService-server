package com.maigo.cloud.xmpp;

public class IQAck extends IQ
{
    public final static String DEFAULT_XMLNS = "maigo.cloud.service:ack";

    private String xmlns;

    private boolean isSuccess;

    public IQAck()
    {
        super("ack", DEFAULT_XMLNS);
    }

    @Override
    protected void onParseNodeText(String nodeName, String text)
    {
        super.onParseNodeText(nodeName, text);
        if(nodeName.equals("success"))
            isSuccess = text.equals("true");
    }

    @Override
    protected void onParseNodeAttr(String nodeName, String attr, String value)
    {
        super.onParseNodeAttr(nodeName, attr, value);
        if(nodeName.equals("ack") && attr.equals("xmlns"))
            xmlns = value;
    }

    @Override
    protected String getIQChildXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<success>");
        stringBuilder.append(String.valueOf(isSuccess));
        stringBuilder.append("</success>");

        return stringBuilder.toString();
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    public boolean isSuccess()
    {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }
}