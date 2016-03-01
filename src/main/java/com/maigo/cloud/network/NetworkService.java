package com.maigo.cloud.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NetworkService
{
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start()
    {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ch.pipeline().addLast(
                                new HeartBeatHandler(),
                                new XMPPDecoder(),
                                new XMPPEncoder(),
                                new XMPPStanzaDispatcher());
                    }
                });
        bootstrap.bind(5222);
    }

    public void stop()
    {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
