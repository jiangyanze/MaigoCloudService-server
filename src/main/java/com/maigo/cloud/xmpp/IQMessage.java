package com.maigo.cloud.xmpp;

/**
 * stanza that contains the info of a transparent message
 */
public class IQMessage extends IQ
{
    public final static String DEFAULT_XMLNS = "maigo.cloud.service:message";

    private String title = "";
    private String content = "";

    public IQMessage()
    {
        super("message", DEFAULT_XMLNS);
    }

    @Override
    protected String getIQChildXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<message>");
        stringBuilder.append("<title>");
        stringBuilder.append(title);
        stringBuilder.append("</title>");
        stringBuilder.append("<content>");
        stringBuilder.append(content);
        stringBuilder.append("</content>");
        stringBuilder.append("</message>");

        return stringBuilder.toString();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
