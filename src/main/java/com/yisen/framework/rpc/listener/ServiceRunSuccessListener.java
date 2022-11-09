package com.yisen.framework.rpc.listener;

import com.yisen.framework.rpc.controller.model.LocalProvider;
import com.yisen.framework.rpc.registry.BaseZookeeperRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.net.InetAddress;

/**
 * @Author : yisen
 * @Date : 2022/11/7 18:00
 * @Description :
 */
@Component
@Slf4j
public class ServiceRunSuccessListener implements ApplicationListener<AvailabilityChangeEvent> {

    @Value("${server.port}")
    private String port;

    private final ServletContext servletContext;

    public ServiceRunSuccessListener(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (ReadinessState.ACCEPTING_TRAFFIC == event.getState()) {
            try {
                //初始化
                initMethodMapper();
                log.info("测试dubbo接口，请访问 http://{}:{}{}/rpc/provider.html", InetAddress.getLocalHost().getHostAddress(), port, servletContext.getContextPath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initMethodMapper() {
        LocalProvider localProvider = LocalProvider.getInstance();
        log.info("ServletContext.ContextPath{}", servletContext.getContextPath());
        BaseZookeeperRegistry.PROVIDER_MAP.values().forEach(value -> localProvider.addClass(value, port, servletContext.getContextPath()));
    }
}