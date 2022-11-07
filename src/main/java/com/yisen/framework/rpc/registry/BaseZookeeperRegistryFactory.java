package com.yisen.framework.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

@Slf4j
public class BaseZookeeperRegistryFactory implements RegistryFactory {

    private ZookeeperTransporter zookeeperTransporter;
    private RegistryFactory registryFactory;


    public BaseZookeeperRegistryFactory(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    @Override
    public Registry getRegistry(URL url) {
        return new BaseZookeeperRegistry(registryFactory.getRegistry(url));
    }
}
