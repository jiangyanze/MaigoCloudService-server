package com.maigo.cloud.network;

import com.maigo.cloud.xmpp.StartTLS;
import com.maigo.cloud.xmpp.XMPPStanzaSplitter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XMPPStanzaSplitterTest
{
    @Test
    public void testSplit()
    {
        String stanza0 = "<stream:stream from='test@123.com' to='456.example.com' version='1.0' xml:lang='en' " +
                "xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>";
        String stanza1 = "<presence><show>away</show><status>meal</status></presence>";
        String stanza2 = "<message from='test_a@jabber.org' to='test_b@jabber.org' type='chat'><body>Hello!</body></message>";
        String stanza3 = "<auth mechanism='DIGEST-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></auth>";
        String stanza4 = "</stream:stream>";

        //insert some space because space will be sent for heart-beat
        String input = "    " + stanza0 + "   " + stanza1 + "     " + stanza2 + stanza3 + stanza4;

        String input1 = input.substring(0, 50);
        String input2 = input.substring(50, 100);
        String input3 = input.substring(100, 165);
        String input4 = input.substring(165, 180);
        String input5 = input.substring(180, 200);
        String input6 = input.substring(200, input.length());

        XMPPStanzaSplitter xmppStanzaSplitter = new XMPPStanzaSplitter();
        List<String> stanzaList;

        xmppStanzaSplitter.split(input1);
        assertFalse(xmppStanzaSplitter.isStanzaAvailable());

        xmppStanzaSplitter.split(input2);
        assertFalse(xmppStanzaSplitter.isStanzaAvailable());

        xmppStanzaSplitter.split(input3);
        assertTrue(xmppStanzaSplitter.isStanzaAvailable());
        stanzaList = xmppStanzaSplitter.getSplitStanzas();
        assertEquals(1, stanzaList.size());
        assertEquals(stanza0, stanzaList.get(0));

        xmppStanzaSplitter.split(input4);
        assertFalse(xmppStanzaSplitter.isStanzaAvailable());

        xmppStanzaSplitter.split(input5);
        assertFalse(xmppStanzaSplitter.isStanzaAvailable());

        xmppStanzaSplitter.split(input6);
        assertTrue(xmppStanzaSplitter.isStanzaAvailable());
        stanzaList = xmppStanzaSplitter.getSplitStanzas();
        assertEquals(4, stanzaList.size());
        assertEquals(stanza1, stanzaList.get(0));
        assertEquals(stanza2, stanzaList.get(1));
        assertEquals(stanza3, stanzaList.get(2));
        assertEquals(stanza4, stanzaList.get(3));
    }
}
