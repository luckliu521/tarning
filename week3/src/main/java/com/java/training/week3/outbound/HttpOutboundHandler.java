package com.java.training.week3.outbound;


import com.java.training.week3.filter.HttpRequestFilter;
import com.java.training.week3.outbound.netty4.NettyHttpClientOutboundHandler;
import com.java.training.week3.router.HttpEndpointRouter;
import com.java.training.week3.router.RandomHttpEndpointRouter;
import com.sun.jndi.toolkit.url.Uri;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HttpOutboundHandler {
    public enum ClientEnum {
        HTTP_ASYNC_CLIENT,
        HTTP_CLIENT,
        OK_HTTL_CLIENT,
        NETTY_CLIENT
    }

    private ClientEnum clientEnum;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    private CloseableHttpAsyncClient httpAsyncClient;
    private CloseableHttpClient httpclient;
    private OkHttpClient okHttpClient;
    private Bootstrap nettyClient;

    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpOutboundHandler(List<String> backends, ClientEnum clientEnum) {
        this.clientEnum = clientEnum;
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

        switch (this.clientEnum) {
            case HTTP_ASYNC_CLIENT: {
                IOReactorConfig ioConfig = IOReactorConfig.custom()
                        .setConnectTimeout(1000)
                        .setSoTimeout(1000)
                        .setIoThreadCount(cores)
                        .setRcvBufSize(32 * 1024)
                        .build();

                httpAsyncClient = HttpAsyncClients.custom().setMaxConnTotal(40)
                        .setMaxConnPerRoute(8)
                        .setDefaultIOReactorConfig(ioConfig)
                        .setKeepAliveStrategy((response, context) -> 6000)
                        .build();
                httpAsyncClient.start();
                System.out.println("HTTP_ASYNC_CLIENT============");
                break;
            }
            case HTTP_CLIENT: {
                //整合上次作业httpclient
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(1000)
                        .setSocketTimeout(1000)
                        .setConnectionRequestTimeout(1000)
                        .build();

                httpclient = HttpClients.custom().setMaxConnTotal(40)
                        .setMaxConnPerRoute(8)
                        .setDefaultRequestConfig(requestConfig)
                        .setKeepAliveStrategy((response, context) -> 6000)
                        .build();
                System.out.println("HTTP_CLIENT============");
                break;
            }
            case OK_HTTL_CLIENT: {
                //整合上次作业okhttpclient
                okHttpClient = new OkHttpClient().newBuilder().build();
                System.out.println("OK_HTTL_CLIENT============");
                break;
            }
            case NETTY_CLIENT:
            default: {
                //使用 netty 实现后端 http 访问
                nettyClient = new Bootstrap();
                nettyClient.channel(NioSocketChannel.class);
                nettyClient.option(ChannelOption.SO_KEEPALIVE, true);
                System.out.println("NETTY_CLIENT OR DEFAULT ============");
                break;
            }
        }
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, List<HttpRequestFilter> filters) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filters.stream().forEach(item -> {
            item.filter(fullRequest, ctx, url);
        });
        fetchGet(fullRequest, ctx, url);
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {

        switch (this.clientEnum) {
            case HTTP_ASYNC_CLIENT: {
                final HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                httpGet.setHeader("mao", inbound.headers().get("mao"));
                httpAsyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(final HttpResponse endpointResponse) {
                        try {
                            new HandleResponse(inbound, ctx, endpointResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }

                    @Override
                    public void failed(final Exception ex) {
                        httpGet.abort();
                        ex.printStackTrace();
                    }

                    @Override
                    public void cancelled() {
                        httpGet.abort();
                    }
                });
                break;
            }
            case HTTP_CLIENT: {
                final HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                httpGet.setHeader("mao", inbound.headers().get("mao"));
                try {
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    new HandleResponse(inbound, ctx, response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case OK_HTTL_CLIENT: {
                final Request request = new Request.Builder()
                        .url(url)
                        .addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        call.cancel();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            new HandleResponse(inbound, ctx, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
                break;
            }
            case NETTY_CLIENT: {
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    nettyClient.group(workerGroup);
                    nettyClient.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                            ch.pipeline().addLast(new HttpResponseDecoder());
                            //客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                            ch.pipeline().addLast(new HttpRequestEncoder());
                            ch.pipeline().addLast(new NettyHttpClientOutboundHandler(inbound, ctx));
                        }
                    });
                    ChannelFuture f = null;
                    try {
                        Uri uri = new Uri(url);
                        f = nettyClient.connect(uri.getHost(), uri.getPort()).sync();
                        f.channel().flush();
                        f.channel().closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } finally {
                    workerGroup.shutdownGracefully();
                }
                break;
            }
            default:
                break;
        }
    }
}
