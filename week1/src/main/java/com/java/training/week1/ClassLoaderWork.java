package com.java.training.week1;

import lombok.SneakyThrows;

import javax.annotation.Resource;
import java.io.*;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/8/6 0:18
 * @className: com.java.training.week1.jvm.Job
 * @description: 读取ClassLoader Demo
 */
public class ClassLoaderWork extends ClassLoader {
    private final static String XLASS_RESOURCE = "Hello.xlass";

    public static void main(String[] args) throws Exception {
        //获得class
        Class helloClass = new ClassLoaderWork().findClass("Hello");
        helloClass.getMethod("hello").invoke(helloClass.newInstance());
    }

    @SneakyThrows
    @Override
    protected Class<?> findClass(String name) {
        //读取xlass文件流
        byte[] helloXlass = readHellXlass(XLASS_RESOURCE);
        //对文件流转码
        byte[] bytes = decode(helloXlass);
        //保存class文件
//        saveHelloClass("F:" + File.separator + "Hello.class", bytes);
        //返回class
        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 对xlass文件流进行转码
     *
     * @param helloXlass
     * @return
     */
    private byte[] decode(byte[] helloXlass) {
        byte[] result = new byte[helloXlass.length];
        for (int i = 0; i < helloXlass.length; i++) {
            result[i] = (byte) (255 - helloXlass[i]);
        }
        return result;
    }

    /**
     * 读取xlass文件流
     *
     * @return
     * @throws IOException
     */
    private byte[] readHellXlass(final String fileName) throws IOException {
        File f = new File(this.getClass().getResource("/") + fileName);
        InputStream in = new FileInputStream(f);
        byte[] hello = new byte[in.available()];
        in.read(hello);
        in.close();
        return hello;
    }

    /**
     * 保存class文件
     *
     * @param fileName
     * @param bytes
     * @throws IOException
     */
    private void saveHelloClass(final String fileName, byte[] bytes) throws IOException {
        File f = new File(fileName);
        OutputStream out = new FileOutputStream(f);
        out.write(bytes);
        out.close();
    }
}
