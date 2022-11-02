package com.yisen.framework.rpc.conf;

import com.yisen.framework.rpc.controller.DubboRestfulController;
import com.yisen.framework.rpc.controller.condition.LoadDubboRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@Slf4j
public class DubboApplicationAutoConfiguration {

    @Bean
    @Conditional(LoadDubboRestController.class)
    public DubboRestfulController registDubboConf() {
        return new DubboRestfulController();
    }
}
