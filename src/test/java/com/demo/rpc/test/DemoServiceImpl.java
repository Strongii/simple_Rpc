package com.demo.rpc.test;

public class DemoServiceImpl implements DemoService {
    public String sayHello(String param) {
        System.out.println("param" + param);
        return "hello:" + param;
    }

    @Override
    public String login(String userName) {
        System.out.println("亲爱的"+userName+"欢迎来到艾欧尼亚,ta是嘴强王者");
        return "亲爱的"+userName+"欢迎来到艾欧尼亚,ta是嘴强王者";
    }
}