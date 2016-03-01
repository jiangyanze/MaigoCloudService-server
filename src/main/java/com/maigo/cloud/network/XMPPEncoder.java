package com.maigo.cloud.network;

import com.maigo.cloud.xmpp.Stanza;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class XMPPEncoder extends ChannelOutboundHandlerAdapter
{
    @Override
    public void write(ChannelHandlerContext ctx, Object object, ChannelPromise promise) throws Exception
    {
        Stanza stanza = (Stanza) object;
        ByteBuf byteBuf = ctx.alloc().buffer();
        System.out.println("[Debug]XMPPEncoder: send stanza = " + stanza.toXmlString());
        byteBuf.writeBytes(stanza.toXmlString().getBytes(Charset.forName("utf-8")));
        ctx.writeAndFlush(byteBuf, promise);
    }
}
