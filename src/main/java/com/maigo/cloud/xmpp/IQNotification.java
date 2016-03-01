package com.maigo.cloud.xmpp;

/**
 * stanza that contains the info of a push notification.
 */
public class IQNotification extends IQ
{
    public final static String DEFAULT_XMLNS = "maigo.cloud.service:notification";

    private String title = "";
    private String content = "";

    public IQNotification()
    {
        super("notification", DEFAULT_XMLNS);
    }

    @Override
    protected String getIQChildXmlString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<notification>");
        stringBuilder.append("<title>");
        stringBuilder.append(title);
        stringBuilder.append("</title>");
        stringBuilder.append("<content>");
        stringBuilder.append(content);
        stringBuilder.append("</content>");
        stringBuilder.append("</notification>");

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
