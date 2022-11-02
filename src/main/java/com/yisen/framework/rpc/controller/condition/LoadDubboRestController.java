package com.yisen.framework.rpc.controller.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


@Slf4j
public class LoadDubboRestController implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (System.getProperty("export_restful") != null) {
            try {
                //当配置export_restful 是启动
                return Boolean.parseBoolean(System.getProperty("export_restful"));
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return false;
    }
}
