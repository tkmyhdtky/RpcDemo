package com.zhao.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zhao.rpc.RpcApplication;
import com.zhao.rpc.model.RpcRequest;
import com.zhao.rpc.model.RpcResponse;
import com.zhao.rpc.serializer.JdkSerializer;
import com.zhao.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("指定序列化器");
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 构造请求
        System.out.println("构造请求信息");
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            System.out.println("请求序列化");
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            String url = "http://localhost:" + RpcApplication.getRpcConfig().getServerPort();
            // 发送请求
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post(url)
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                System.out.println("内容反序列化");
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
