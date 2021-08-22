package com.java.training.week3.filter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/8/22 17:34
 * @className: com.java.training.week3.filiter.AuthHttpRequestFilter
 * @description: 授权Filiter
 */
public class AuthHttpRequestFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx, String url) {
        if (!url.contains("auth")) {
            System.out.println("request auth ok");
        } else {
            FullHttpResponse response = null;
            try {
                String errorMsg = "非法URL";
                byte[] body = errorMsg.getBytes();
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                response.headers().set("Content-Type", "application/json");
                response.headers().setInt("Content-Length", body.length);
            } finally {
                if (fullRequest != null) {
                    if (!HttpUtil.isKeepAlive(fullRequest)) {
                        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        ctx.write(response);
                    }
                }
                ctx.flush();
            }
        }
    }
}
