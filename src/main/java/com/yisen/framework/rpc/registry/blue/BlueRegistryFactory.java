package com.yisen.framework.rpc.registry.blue;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

public class BlueRegistryFactory extends AbstractRegistryFactory {

    private ZookeeperTransporter zookeeperTransporter;

    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    public Registry createRegistry(URL url) {
        return new BlueRegistry(url, zookeeperTransporter);
    }
}
