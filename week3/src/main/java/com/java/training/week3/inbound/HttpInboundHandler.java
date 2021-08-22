package com.java.training.week3.inbound;

import com.java.training.week3.filter.AuthHttpRequestFilter;
import com.java.training.week3.filter.HeaderHttpRequestFilter;
import com.java.training.week3.filter.HttpRequestFilter;
import com.java.training.week3.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final List<String> proxyServer;
    private HttpOutboundHandler handler;
    private HttpRequestFilter filter = new HeaderHttpRequestFilter();
    private AuthHttpRequestFilter authFilter = new AuthHttpRequestFilter();

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new HttpOutboundHandler(this.proxyServer, HttpOutboundHandler.ClientEnum.HTTP_ASYNC_CLIENT);
//        this.handler = new HttpOutboundHandler(this.proxyServer, HttpOutboundHandler.ClientEnum.HTTP_CLIENT);
//        this.handler = new HttpOutboundHandler(this.proxyServer, HttpOutboundHandler.ClientEnum.OK_HTTL_CLIENT);
//        this.handler = new HttpOutboundHandler(this.proxyServer, HttpOutboundHandler.ClientEnum.NETTY_CLIENT);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            List<HttpRequestFilter> filters = new ArrayList<>();
            filters.add(filter);
            filters.add(authFilter);
            handler.handle(fullRequest, ctx, filters);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
