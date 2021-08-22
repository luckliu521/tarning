package com.java.training.week3.outbound;

import com.java.training.week3.filter.HeaderHttpResponseFilter;
import com.java.training.week3.filter.HttpResponseFilter;
import com.java.training.week3.filter.SignHttpResponseFilter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/8/22 17:44
 * @className: com.java.training.week3.outbound.HandleResponse
 * @description: TODO
 */
public class HandleResponse {

    private FullHttpResponse response = null;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    SignHttpResponseFilter signFilter = new SignHttpResponseFilter();

    public HandleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpResponse httpResponse) {
        try {
            byte[] body = EntityUtils.toByteArray(httpResponse.getEntity());
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().setInt("Content-Length", Integer.parseInt(httpResponse.getFirstHeader("Content-Length").getValue()));
            this.handleResponse(fullRequest, ctx, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HandleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, Response okResponse) {
        byte[] body = new byte[0];
        try {
            body = okResponse.body().bytes();
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().setInt("Content-Length", Integer.parseInt(okResponse.header("Content-Length")));
            this.handleResponse(fullRequest, ctx, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HandleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpContent nettyResponse, int len) {
        byte[] bytes = new byte[len];
        ByteBuf tmpDatas = nettyResponse.content();
        tmpDatas.readBytes(bytes);
        tmpDatas.release();
        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
        response.headers().setInt("Content-Length", bytes.length);
        this.handleResponse(fullRequest, ctx, response);
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, FullHttpResponse response) {
        try {
            response.headers().set("Content-Type", "application/json");
            filter.filter(response);
            signFilter.filter(response);
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            ctx.write(response);
            exceptionCaught(ctx, e);
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

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
