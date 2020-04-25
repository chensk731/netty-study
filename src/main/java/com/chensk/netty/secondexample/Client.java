package com.chensk.netty.secondexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    public static void main( String[] args ) throws Exception {
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try{
            Bootstrap bootstrap=new Bootstrap();
            Channel channel= bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ClientInitializer()).connect("localhost",6666).sync().channel();
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                channel.writeAndFlush(br.readLine()+"\r\n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }


    }
}
