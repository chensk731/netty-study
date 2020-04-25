package com.chensk.netty.secondexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel( SocketChannel ch ) throws Exception {
        ChannelPipeline channelPipeline=ch.pipeline();
        //消息截取，\r,\n为截断符
        channelPipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        //消息解码
        channelPipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //消息编码
        channelPipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自定义消息处理
        channelPipeline.addLast(new ClientHandler());
    }
}
