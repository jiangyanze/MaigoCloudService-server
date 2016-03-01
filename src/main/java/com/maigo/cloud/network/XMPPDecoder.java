package com.maigo.cloud.network;

import com.maigo.cloud.xmpp.XMPPStanzaSplitter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class XMPPDecoder extends MessageToMessageDecoder<ByteBuf>
{
    XMPPStanzaSplitter xmppStanzaSplitter = new XMPPStanzaSplitter();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception
    {
        xmppStanzaSplitter.split(byteBuf.toString(Charset.defaultCharset()));
        if(xmppStanzaSplitter.isStanzaAvailable())
        {
            out.addAll(xmppStanzaSplitter.getSplitStanzas());
        }
    }
}
