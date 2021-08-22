package com.java.training.week3.filter;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/8/22 17:54
 * @className: com.java.training.week3.filiter.SignHttpResponseFilter
 * @description: 签名Filiter
 */
public class SignHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        FullHttpResponse httpResponse = response.copy();
        ByteBuf content = httpResponse.content();
        byte[] byteContent = new byte[content.readableBytes()];
        content.readBytes(byteContent);
        content.release();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] byteSign = md.digest(byteContent);
            response.headers().set("sign", byteArrayToHexStr(byteSign));
            System.out.println("response sign ok");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
