package com.yisen.framework.rpc.registry;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.rpc.cluster.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.dubbo.rpc.Constants.STUB_KEY;

@SuppressWarnings("rawtypes")
public class BaseZookeeperRegistry implements Registry {

    private static final Logger logger = LoggerFactory.getLogger(BaseZookeeperRegistry.class);
    private static final Map<String, ReferenceConfig> localCallerReferMap = new ConcurrentHashMap<>();

    public static Map<String, Class> PROVIDER_MAP = new ConcurrentHashMap<>();
    public static Map<String, Class> CONSUMER_MAP = new ConcurrentHashMap<>();
    private String protocol;
    private String zkAddr;
    private Registry originRegistry;

    public BaseZookeeperRegistry(Registry originRegistry) {
        this.originRegistry = originRegistry;
    }

    @Override
    public URL getUrl() {
        return originRegistry.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return originRegistry.isAvailable();
    }

    @Override
    public void destroy() {
        originRegistry.destroy();
    }

    @Override
    public void register(URL url) {
        String side = url.getParameter(CommonConstants.SIDE_KEY);
        if (CommonConstants.PROVIDER_SIDE.equals(side) && StringUtils.isEmpty(url.getParameter(Constants.RETRIES_KEY))) {
            url = url.addParameter(Constants.RETRIES_KEY, 0);
        }
        originRegistry.register(url);
        String className = url.getPath();
        if (className.startsWith("com.alibaba") || className.startsWith("org.apache.dubbo")) {
            return;
        }
        String protocol = url.getProtocol();
        try {
            if (protocol.equals(CommonConstants.CONSUMER)) {
                CONSUMER_MAP.put(className, Class.forName(className));
            } else if (CommonConstants.PROVIDER_SIDE.equals(side)) {
                PROVIDER_MAP.put(className, Class.forName(className));
                String localCallerName = getCreateLocalCallerName(className);

                if (localCallerReferMap.get(localCallerName) == null && Boolean.parseBoolean(System.getProperty("export_restful"))) {
                    //ApplicationConfig配置
                    ApplicationConfig application = new ApplicationConfig();
                    application.setName(url.getParameter(CommonConstants.APPLICATION_KEY, localCallerName));

                    //RegistryConfig配置
                    RegistryConfig registry = new RegistryConfig();
                    registry.setAddress(this.protocol + "://" + this.zkAddr);
                    registry.setProtocol(this.protocol);

                    //消费者ReferenceConfig
                    ReferenceConfig reference = new ReferenceConfig();
                    reference.setApplication(application);
                    reference.setRetries(url.getParameter(Constants.RETRIES_KEY, 0));
                    reference.setGroup(url.getParameter(CommonConstants.GROUP_KEY, ""));
                    reference.setVersion(url.getParameter(CommonConstants.VERSION_KEY, ""));
                    reference.setTimeout(Integer.valueOf(url.getParameter(CommonConstants.TIMEOUT_KEY, "3000")));
                    reference.setStub(Boolean.valueOf(url.getParameter(STUB_KEY, "false")));
                    reference.setRegistry(registry);
                    reference.setInterface(className);
                    reference.setUrl("dubbo://" + url.getAddress() + "?" + CommonConstants.VERSION_KEY + "=" + url.getParameter(CommonConstants.VERSION_KEY));

                    localCallerReferMap.put(localCallerName, reference);
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        }
        logger.info("do base zk register done");
    }

    @Override
    public void unregister(URL url) {
        originRegistry.unregister(url);
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        originRegistry.subscribe(url, listener);
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        originRegistry.unsubscribe(url, listener);
    }

    @Override
    public List<URL> lookup(URL url) {
        return originRegistry.lookup(url);
    }

    private static String getCreateLocalCallerName(String className) {
        return className + ".local.consumer";
    }

    public static <T> T getCaller(Class<T> refer) {
        if (refer == null)
            return null;
        @SuppressWarnings("unchecked")
        ReferenceConfig<T> referenceConfig = localCallerReferMap.get(getCreateLocalCallerName(refer.getName()));
        return referenceConfig.get();
    }
}
