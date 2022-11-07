package com.yisen.framework.rpc.registry.green;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistry;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

import java.util.List;

public class GreenRegistry extends ZookeeperRegistry {

    public GreenRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        super(url, zookeeperTransporter);
    }

    private URL addGroup(URL url) {
        if (StringUtils.isNotEmpty(url.getServiceInterface())
                && !url.getServiceInterface().equalsIgnoreCase("org.apache.dubbo.monitor.MonitorService")) {
            String side = url.getParameter(CommonConstants.SIDE_KEY);
            if (CommonConstants.PROVIDER_SIDE.equals(side) || CommonConstants.CONSUMER_SIDE.equals(side)) {
                url = url.addParameter(CommonConstants.GROUP_KEY, "green");
            }
        }
        return url;
    }

    @Override
    public void doRegister(URL url) {
        super.doRegister(url);
    }

    @Override
    public void doUnregister(URL url) {
        super.doUnregister(url);
    }

    @Override
    public void doSubscribe(URL url, NotifyListener listener) {
        super.doSubscribe(url, listener);
    }

    @Override
    public void doUnsubscribe(URL url, NotifyListener listener) {
        super.doUnsubscribe(url, listener);
    }

    @Override
    public List<URL> lookup(URL url) {
        return super.lookup(addGroup(url));
    }

    @Override
    public void register(URL url) {
        super.register(addGroup(url));
    }

    @Override
    public void unregister(URL url) {
        super.unregister(addGroup(url));
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        super.subscribe(addGroup(url), listener);
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        super.unsubscribe(addGroup(url), listener);
    }

    @Override
    protected void notify(URL url, NotifyListener listener, List<URL> urls) {
        super.notify(url, listener, urls);
    }

    @Override
    protected void doNotify(URL url, NotifyListener listener, List<URL> urls) {
        super.doNotify(url, listener, urls);
    }

    @Override
    protected void setUrl(URL url) {
        super.setUrl(url);
    }

    @Override
    public List<URL> getCacheUrls(URL url) {
        return super.getCacheUrls(url);
    }

    @Override
    protected void notify(List<URL> urls) {
        super.notify(urls);
    }

}
