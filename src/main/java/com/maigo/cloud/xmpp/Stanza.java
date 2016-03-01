package com.maigo.cloud.xmpp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringBufferInputStream;

public abstract class Stanza
{
    protected String from;
    protected String to;
    protected String id;
    protected String type;

    private String currentQName = "";

    public void parse(String stanza)
    {
        try
        {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            DefaultHandler defaultHandler = new DefaultHandler()
            {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
                {
                    currentQName = qName;

                    int attrCount = attributes.getLength();
                    for(int i=0; i<attrCount; i++)
                    {
                        String attrName = attributes.getQName(i);
                        String attrValue = attributes.getValue(i);

                        if(attrName.equals("from"))
                            from = attrValue;
                        else if(attrName.equals("to"))
                            to = attrValue;
                        else if(attrName.equals("id"))
                            id = attrValue;
                        else if(attrName.equals("type"))
                            type = attrValue;
                        else
                            onParseNodeAttr(currentQName, attrName, attrValue);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException
                {
                    onParseNodeText(currentQName, new String(ch, start, length));
                }
            };

            saxParser.parse(new StringBufferInputStream(stanza), defaultHandler);
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void onParseNodeText(String nodeName, String text);

    protected abstract void onParseNodeAttr(String nodeName, String attr, String value);

    public String getFrom()
    {
        return from;
    }

    public String getTo()
    {
        return to;
    }

    public String getId()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public abstract String toXmlString();
}
