package com.chensk.netty.secondexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main( String[] args ) {
        //用于处理连接的事件循环组
        EventLoopGroup parentGroup=new NioEventLoopGroup();
        //用于处理消息的事件循环组
        EventLoopGroup childGroup=new NioEventLoopGroup();
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            ChannelFuture channelFuture= serverBootstrap.group(parentGroup,childGroup).channel(NioServerSocketChannel.class).childHandler(new ServerInitializer()).bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }


    }
}
