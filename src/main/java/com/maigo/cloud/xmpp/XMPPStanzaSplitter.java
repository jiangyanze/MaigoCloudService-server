package com.maigo.cloud.xmpp;

import java.util.ArrayList;
import java.util.List;

public class XMPPStanzaSplitter
{
    public final static boolean ALLOW_DEBUG = true;
    public final static boolean ALLOW_DEBUG_DETAIL = false;
    public final static String DEBUG_PREFIX = "[Debug]XMPPStanzaSplitter: ";

    //the following example to explain the state are base on this XML and 'X' is the scanning position:
    /*
        <book name='abc'>
            <type id='12' />
            <price>10</price>
        </book>
     */
    public final static int STATE_INIT = 0;         //state after initialize or just split a stanza
    public final static int STATE_HEAD = 1;         //state scanning in the head tag name ex:<boXk name='abc'><...
    public final static int STATE_INSIDE = 2;       //state scanning after the head tag name ex:<bookXname='abc'><...
    public final static int STATE_SELF_CLOSE = 3;   //state found a '/' and looking for the '>' ex:...<type id='12' X>...
    public final static int STATE_CLOSE = 4;        //state found a '/' and looking for the '>' ex:...<price>10<Xprice>...

    private int state = STATE_INIT;

    private StringBuilder bufferStringBuilder;      //buffer to split and save the content which is not split
    private int bufferPosition;                     //the position of the buffer which is split
    private int readDepth;                          //the depth of the current read document
    private StringBuilder headStringBuilder;        //store the head tag
    private int remainedLength;

    private List<String> splitStanzaList;

    public XMPPStanzaSplitter()
    {
        init();
    }

    public void init()
    {
        state = STATE_INIT;

        bufferStringBuilder = new StringBuilder();
        headStringBuilder = new StringBuilder();
        splitStanzaList = new ArrayList<String>();
    }

    public void split(String input)
    {
        prepareBuffer();
        char[] charArray = input.toCharArray();
        int remaining = charArray.length;

        if(remaining == 0)
            return;

        bufferStringBuilder.append(input);

        char ch;
        for(int i=0; i<remaining; i++)
        {
            ch = charArray[i];
            if(ALLOW_DEBUG && ALLOW_DEBUG_DETAIL)
                System.out.println(DEBUG_PREFIX + "scanning char '" + ch + "'" + " state = " + state
                        + " readDepth = " + readDepth + " position = " + i + " bufferPosition = " + bufferPosition);
            switch (state)
            {
                case STATE_INIT:
                    processCharAtInitState(ch, i);
                    break;
                case STATE_HEAD:
                    processCharAtHeadState(ch, i);
                    break;
                case STATE_INSIDE:
                    processCharAtInsideState(ch, i);
                    break;
                case STATE_SELF_CLOSE:
                    processCharAtSelfCloseState(ch, i);
                    break;
                case STATE_CLOSE:
                    processCharAtCloseState(ch, i);
                    break;
            }
        }
    }

    private void processCharAtInitState(char ch, int position)
    {
        if(ch == '<')
        {
            state = STATE_HEAD;
            headStringBuilder = new StringBuilder();
            headStringBuilder.append('<');
            readDepth++;
        }
        else if(readDepth == 0)
        {
            bufferPosition++;
        }
    }

    private void processCharAtHeadState(char ch, int position)
    {
        if(ch == '/')
        {
            if(headStringBuilder.length() > 1) {
                state = STATE_SELF_CLOSE;
            }
            else
            {
                state = STATE_CLOSE;
                readDepth--;
            }
        }
        else if(ch == ' ')
        {
            headStringBuilder.append('>');
            state = STATE_INSIDE;
            return;
        }
        else if(ch == '>')
        {
            state = STATE_INIT;
        }
        else if(ch == '?' || ch == '!')
        {
            throw new UnsupportedOperationException("<? .....> or <!-- --> is not support yet!");
        }

        headStringBuilder.append(ch);
    }

    private void processCharAtInsideState(char ch, int position)
    {
        if(ch == '>')       //found a entire start tag
        {
            if(headStringBuilder.toString().equals("<stream:stream>"))      //found the start tag of a XMPP stream
            {
                foundStanza(position);
            }
            else                                                            //found an entire start tag
            {
                state = STATE_INIT;
            }
        }
        else if(ch == '/')
        {
            state = STATE_SELF_CLOSE;
        }
    }

    private void processCharAtSelfCloseState(char ch, int position)
    {
        if(ch == '>')
        {
            readDepth--;
            if(readDepth == 0)
            {
                foundStanza(position);
            }
        }
    }

    private void processCharAtCloseState(char ch, int position)
    {
        if(ch == '>')
        {
            if(headStringBuilder.toString().equals("</") && readDepth == 0)
            {
                //found the </stream:stream> tag
                foundStanza(position);
            }

            readDepth--;
            if(readDepth == 0)
            {
                foundStanza(position);
            }
            state = STATE_INIT;
        }
    }

    private void prepareBuffer()
    {
        if(bufferStringBuilder.length() > 0)
        {
            if(bufferStringBuilder.length() > 1024 * 1024)
                throw new IllegalStateException("the length of input stanza is over 1MB.");

            String string = bufferStringBuilder.substring(bufferPosition);
            bufferStringBuilder.delete(0, bufferStringBuilder.length());
            bufferStringBuilder.append(string);
            bufferStringBuilder.trimToSize();
        }
        bufferPosition = 0;
        remainedLength = bufferStringBuilder.length();

        if(ALLOW_DEBUG && ALLOW_DEBUG_DETAIL)
            System.out.println(DEBUG_PREFIX + "prepare buffer = [" + bufferStringBuilder.toString() + "] length = "
                    + bufferStringBuilder.length());
    }

    private void foundStanza(int endPosition)
    {
        String stanza;
            stanza = bufferStringBuilder.substring(bufferPosition, remainedLength + endPosition + 1);

        state = STATE_INIT;
        readDepth = 0;
        bufferPosition = remainedLength + endPosition + 1;
        headStringBuilder = new StringBuilder();
        stanza.trim();
        splitStanzaList.add(stanza);

        if(ALLOW_DEBUG)
            System.out.println(DEBUG_PREFIX + "found stanza = " + stanza);
    }

    public boolean isStanzaAvailable()
    {
        return !splitStanzaList.isEmpty();
    }

    public List<String> getSplitStanzas()
    {
        List<String> list = new ArrayList<String>(splitStanzaList);   //return a new List which have the same content
        splitStanzaList.clear();
        return list;
    }
}
