package com.demo.rpc.test;

public class ConsumerOwnlmpl implements ConsumerOwn {
    @Override
    public String ConsumerOwnCall(String name) {
        return "这是ConsumerOwnCall方法，name为："+name;
    }
}
