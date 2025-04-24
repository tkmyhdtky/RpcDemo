package com.zhao.example.provider;


import cn.hutool.log.Log;
import com.zhao.example.common.service.UserService;
import com.zhao.rpc.registry.LocalRegistry;
import com.zhao.rpc.server.HttpServer;
import com.zhao.rpc.server.VertxHttpServer;

import java.util.logging.Logger;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        System.out.println("provider注册服务");
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        System.out.println("provider启动服务");
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
