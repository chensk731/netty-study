package com.chensk.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class Server {
    public static void main( String[] args ) {
        //用于处理连接的事件循环组
        EventLoopGroup parentGroup=new NioEventLoopGroup();
        //用于处理消息的事件循环组
        EventLoopGroup childGroup=new NioEventLoopGroup();
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            ChannelFuture channelFuture= serverBootstrap.group(parentGroup,childGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelHandler(){
                public void handlerAdded( ChannelHandlerContext ctx ) throws Exception {
                    System.out.println("有新的连接进来:"+ctx);
                    ctx.pipeline().addLast("http",new HttpServerCodec());
                    ctx.pipeline().addLast("test", new SimpleChannelInboundHandler<HttpObject>() {
                        @Override
                        protected void channelRead0( ChannelHandlerContext channelHandlerContext, HttpObject httpObject ) throws Exception {
                            ByteBuf cotent=Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8);
                            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,cotent);
                            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
                            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,cotent.readableBytes());
                            channelHandlerContext.channel().writeAndFlush(response);
                        }
                    });
                }

                public void handlerRemoved( ChannelHandlerContext ctx ) throws Exception {
                    System.out.println("断开连接:"+ctx);
                }

                public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
                    System.out.println("抛出异常");
                }
            } ).bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }


    }
}
