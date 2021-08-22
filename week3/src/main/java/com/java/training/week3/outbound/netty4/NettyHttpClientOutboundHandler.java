package com.java.training.week3.outbound.netty4;

import com.java.training.week3.filter.HeaderHttpResponseFilter;
import com.java.training.week3.filter.HttpResponseFilter;
import com.java.training.week3.outbound.HandleResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private final FullHttpRequest fullRequest;
    private final ChannelHandlerContext channelHandlerContext;
    private final HttpResponseFilter filter = new HeaderHttpResponseFilter();
    private int len;

    public NettyHttpClientOutboundHandler(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        this.fullRequest = fullRequest;
        this.channelHandlerContext = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf content = null;
        HttpResponse httpResponse;
        int conLen = 0;
        if (msg instanceof HttpResponse) {
            httpResponse = (HttpResponse) msg;
            len = Integer.parseInt(httpResponse.headers().get(HttpHeaderNames.CONTENT_LENGTH));
        } else if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            new HandleResponse(fullRequest, channelHandlerContext, httpContent, len);
        }
    }

}