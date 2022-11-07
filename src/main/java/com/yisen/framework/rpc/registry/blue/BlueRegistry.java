package com.yisen.framework.rpc.registry.blue;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistry;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

import java.util.List;


public class BlueRegistry extends ZookeeperRegistry {

    public BlueRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        super(url, zookeeperTransporter);
    }

    private URL addGroup(URL url) {
        String side = url.getParameter(CommonConstants.SIDE_KEY);
        if (CommonConstants.PROVIDER_SIDE.equals(side) || CommonConstants.CONSUMER_SIDE.equals(side)) {
            url = url.addParameter(CommonConstants.GROUP_KEY, "blue");
        }
        return url;
    }

    @Override
    public void doRegister(URL url) {
        super.doRegister(addGroup(url));
    }

    @Override
    public void doUnregister(URL url) {
        super.doUnregister(addGroup(url));
    }

    @Override
    public void doSubscribe(URL url, NotifyListener listener) {
        super.doSubscribe(addGroup(url), listener);
    }

    @Override
    public void doUnsubscribe(URL url, NotifyListener listener) {
        super.doUnsubscribe(addGroup(url), listener);
    }

    @Override
    public List<URL> lookup(URL url) {
        return super.lookup(addGroup(url));
    }
}
