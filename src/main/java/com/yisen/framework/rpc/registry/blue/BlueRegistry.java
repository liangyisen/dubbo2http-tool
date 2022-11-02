package com.yisen.framework.rpc.registry.blue;

import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistry;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

import java.util.List;


public class BlueRegistry extends ZookeeperRegistry {

    public BlueRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        super(url, zookeeperTransporter);
    }

    private URL addGroup(URL url) {
        String side = url.getParameter(Constants.SIDE_KEY);
        if (Constants.PROVIDER_SIDE.equals(side) || Constants.CONSUMER_SIDE.equals(side)) {
            url = url.addParameter(Constants.GROUP_KEY, "blue");
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
