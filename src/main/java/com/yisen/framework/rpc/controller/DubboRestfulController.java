package com.yisen.framework.rpc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yisen.framework.rpc.controller.model.LocalProvider;
import com.yisen.framework.rpc.controller.model.LocalProviderDetails;
import com.yisen.framework.rpc.controller.model.ModuleParser;
import com.yisen.framework.rpc.registry.BaseZookeeperRegistry;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rpc")
public class DubboRestfulController {

    @Resource
    private Configuration mConfiguration;

    @GetMapping(value = "/provider.html", produces = "text/html;charset=UTF-8")
    public String getProviderMethodList() throws Exception {
        Map<String, LocalProvider> root = new HashMap<>();
        root.put("localProvider", LocalProvider.getInstance());
        StringWriter sw = new StringWriter(5000);
        mConfiguration.getTemplate("allInterface.ftl").process(root, sw);
        return sw.toString();
    }

    @GetMapping(value = "/consumer/")
    public Map<String, Class> getConsumerMethodList() {
        return BaseZookeeperRegistry.CONSUMER_MAP;
    }

    @PostMapping(value = "/call/{className}/{methodName}")
    public Object getConsumerMethodList(@PathVariable String className, @PathVariable String methodName, @RequestBody List<Object> inputParams) throws Exception {

        Class referClass = BaseZookeeperRegistry.PROVIDER_MAP.get(className);
        Object caller = BaseZookeeperRegistry.getCaller(referClass);
        if (referClass == null || caller == null) {
            throw new NullPointerException("no such method please check the class name");
        }
        LocalProviderDetails localProviderDetails = LocalProvider.getInstance().getLocalProviderDetailsMap().get(className);
        Method method = localProviderDetails.getRestfullToMethod().get(methodName);
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return method.invoke(caller);
        } else {
            Object[] objs = new Object[parameterTypes.length];
            ModuleParser moduleParser = new ModuleParser();
            for (int i = 0; i < parameterTypes.length; i++) {
                Type actualType = method.getGenericParameterTypes()[i];

                if (parameterTypes[i] != null) {
                    if (inputParams.get(i) instanceof List) {
                        objs[i] = moduleParser.handleJSONArray((List) inputParams.get(i),
                                moduleParser.getActualClass(actualType));
                    } else if (inputParams.get(i) instanceof Map) {
                        JSONObject jObj = new JSONObject((Map) inputParams.get(i));
                        objs[i] = JSON.parseObject(jObj.toJSONString(), parameterTypes[i]);
                    } else {
                        Object readValue = new ObjectMapper().readValue("" + inputParams.get(i), parameterTypes[i]);
                        objs[i] = readValue;
                    }
                }
            }
            return method.invoke(caller, objs);
        }
    }

    @GetMapping(value = "/get/{className}/{methodName}", produces = "text/html;charset=UTF-8")
    public String getMethodInfo(@PathVariable String className, @PathVariable String methodName) throws Exception {

        LocalProviderDetails localProviderDetails = LocalProvider.getInstance().getLocalProviderDetailsMap().get(className);
        Method method = localProviderDetails.getRestfullToMethod().get(methodName);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        ModuleParser moduleParser = new ModuleParser();
        Object returnClass = moduleParser.constructTheClass(returnType);
        String returnShouldBe = "";
        if (returnClass != null) {
            returnClass = moduleParser.initObj(returnClass);
            returnShouldBe = JSON.toJSONString(returnClass, true);
        }
        StringBuilder requestShouldBe = new StringBuilder();
        requestShouldBe.append("[");
        for (int i = 0; i < parameterTypes.length; i++) {
            Object prarm = moduleParser.initMethodParam(method, i);
            requestShouldBe.append(JSON.toJSONString(prarm, true));
            if (i != parameterTypes.length - 1) {
                requestShouldBe.append(",");
            }
        }
        requestShouldBe.append("]");
        String[] details = new String[]{requestShouldBe.toString(), returnShouldBe, localProviderDetails.getRestfullToMethodDesc().get(methodName)[2]};
        Map<String, String[]> root = new HashMap<>();
        root.put("details", details);
        StringWriter sw = new StringWriter(5000);
        mConfiguration.getTemplate("methodDetails.ftl").process(root, sw);
        return sw.toString();
    }

}
