package com.java.training.week2;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/8/13 23:54
 * @className: com.java.training.week2.HttpClientWork
 * @description: TODO
 */
public class HttpClientWork {
    private final static String LOCAL_URL = "http://localhost:8801";


    public static void main(String[] args) throws IOException {
        // 创建HttpClient
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000)
                .setSocketTimeout(1000)
                .setConnectionRequestTimeout(1000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultRequestConfig(requestConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();
        CloseableHttpResponse response = null;
        try {
            //创建HttpGet
            HttpGet httpGet = new HttpGet(LOCAL_URL);
            httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            response = httpClient.execute(httpGet);
            //获取响应
            HttpEntity entity = response.getEntity();
            System.out.println("=================" + response.getStatusLine());
            if (entity != null) {
                System.out.println("==========: " + EntityUtils.toString(entity));
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
    }
}
