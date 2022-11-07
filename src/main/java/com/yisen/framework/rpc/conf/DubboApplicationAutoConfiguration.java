package com.yisen.framework.rpc.conf;

import com.yisen.framework.rpc.controller.DubboRestfulController;
import com.yisen.framework.rpc.controller.condition.LoadDubboRestController;
import freemarker.template.Configuration;
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

    @Bean
    @Conditional(LoadDubboRestController.class)
    public Configuration mConfiguration() {
        Configuration mConfiguration = new Configuration(Configuration.VERSION_2_3_22);
        mConfiguration.setDefaultEncoding("UTF-8");
        mConfiguration.setClassForTemplateLoading(DubboRestfulController.class, "/framework_template");
        return mConfiguration;
    }

}
