package com.demo.rpc.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import com.google.common.collect.Maps;

public class ZookeeperRegistry<T> implements Registry<T> {

    private Map<String, List<ServiceInstanceListener<T>>> listeners = Maps.newConcurrentMap();

    private Map<String,ServiceCache<T>> nodeServiceNodeCache = Maps.newConcurrentMap();

    private InstanceSerializer serializer = new JsonInstanceSerializer<>(ServerInfo.class);

    private ServiceDiscovery<T> serviceDiscovery;

    private String address = "localhost:2181";

    public void start() throws Exception {
        String root = "/demo/rpc";
        // 初始化CuratorFramework
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
        client.start();  // 启动Curator客户端
        // client.createContainers(root);

        // 初始化ServiceDiscovery
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServerInfo.class)
                .client(client).basePath(root)
                .serializer(serializer)
                .build();
        serviceDiscovery.start(); // 启动ServiceDiscovery
        List<String> listerNodeList = new ArrayList<>();
        listerNodeList.add("/consumerOwn");
        listerNodeList.add("/demoService");

//        client.start(); // 启动Curator客户端
        client.blockUntilConnected();  // 阻塞当前线程，等待连接成功
        serviceDiscovery.start(); // 启动ServiceDiscovery

        for (String listerNode : listerNodeList){
            // 创建ServiceCache，监Zookeeper相应节点的变化，也方便后续的读取
            ServiceCache<T> serviceCache = serviceDiscovery.serviceCacheBuilder()
                    .name(listerNode)
                    .build();
            serviceCache.start(); // 启动ServiceCache
            nodeServiceNodeCache.put(listerNode.substring(1),serviceCache);
        }

    }

    @Override
    public void registerService(ServiceInstance<T> service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    @Override
    public void unregisterService(ServiceInstance service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public List<ServiceInstance<T>> queryForInstances(String name) throws Exception {
        ServiceCache<T> serviceCache = nodeServiceNodeCache.get(name);
        return serviceCache.getInstances().stream()
                .filter(s -> s.getName().equals(name))
                .collect(Collectors.toList());
    }
}
