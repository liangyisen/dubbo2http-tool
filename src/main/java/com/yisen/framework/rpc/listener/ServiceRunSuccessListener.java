package com.yisen.framework.rpc.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (ReadinessState.ACCEPTING_TRAFFIC == event.getState()) {
            try {
                log.info("测试dubbo接口，请访问http://{}:{}/rpc/provider.html", InetAddress.getLocalHost().getHostAddress(), port);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }
}