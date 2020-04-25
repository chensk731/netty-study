package com.chensk.netty.secondexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    //ChannelGroup 是nettty自己管理的，断开连接后自动删除，由事件触发
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0( ChannelHandlerContext ctx, String msg ) throws Exception {
            for(Channel channel:channelGroup){
                if(channel==ctx.channel()){
                    continue;
                }
                channel.writeAndFlush(ctx.channel().remoteAddress()+":"+msg+"\n");
            }
    }
    @Override
    public void handlerAdded( ChannelHandlerContext ctx ) throws Exception {
        channelGroup.writeAndFlush("客户端:"+ctx.channel().remoteAddress()+"已连接\n");
        channelGroup.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved( ChannelHandlerContext ctx ) throws Exception {
        channelGroup.writeAndFlush("服务器:"+ctx.channel().remoteAddress()+"已断开\n");
        super.handlerRemoved(ctx);
    }
    @Override
    public void channelRegistered( ChannelHandlerContext ctx ) throws Exception {
        System.out.println("channelRegistered   "+ctx.channel().remoteAddress());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered( ChannelHandlerContext ctx ) throws Exception {
        System.out.println("channelUnregistered:"+ctx.channel().remoteAddress());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        System.out.println("channelInactive:"+ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete( ChannelHandlerContext ctx ) throws Exception {
        System.out.println("channelReadComplete"+ctx.channel().remoteAddress());
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered( ChannelHandlerContext ctx, Object evt ) throws Exception {
        System.out.println("userEventTriggered"+ctx.channel().remoteAddress());
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
     //   cause.printStackTrace();
        ctx.channel().close();
        //super.exceptionCaught(ctx, cause);
    }

}
