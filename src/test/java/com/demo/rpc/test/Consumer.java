package com.demo.rpc.test;

import com.demo.rpc.proxy.DemoRpcProxy;
import com.demo.rpc.registry.ServerInfo;
import com.demo.rpc.registry.ZookeeperRegistry;

public class Consumer {
    public static void main(String[] args) throws Exception {
        // 创建ZookeeperRegistr对象
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();

        // 创建代理对象，通过代理调用远端Server
        DemoService demoService = DemoRpcProxy.newInstance(DemoService.class, discovery);
        // 调用sayHello()方法，并输出结果
//        for (int i=0;i<1000;i++) {
            String result = demoService.sayHello("hello 牛王 666");
            System.out.println(result);
//            show();
//            String result1 = demoService.login("张三666");
//            System.out.println(result1);
//            show();
//            ConsumerOwn consumerOwn = DemoRpcProxy.newInstance(ConsumerOwn.class, discovery);
//            System.out.println(consumerOwn.ConsumerOwnCall("张三"));
//            show();
//        }

         Thread.sleep(10000000L);
    }
}