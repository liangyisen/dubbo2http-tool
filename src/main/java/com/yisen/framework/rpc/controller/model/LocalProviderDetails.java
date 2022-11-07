package com.yisen.framework.rpc.controller.model;


import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LocalProviderDetails {

    private final Map<String, Method> restfullToMethod = new HashMap<>();
    private final Map<String, String[]> restfullToMethodDesc = new HashMap<>();

    private final LocalProvider localProvider;
    private final String className;

    public LocalProviderDetails(Class cls, LocalProvider localProvider) {
        this.localProvider = localProvider;
        this.className = cls.getName();
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();

            if (restfullToMethod.get(methodName) != null) {
                methodName = methodName + "_" + getParameters(method);
            }
            restfullToMethod.put(methodName, method);
            makeDescription(methodName, method);
        }
    }

    private void makeDescription(String methodName, Method method) {
        String url = "";
        if (StringUtils.isEmpty(localProvider.getServletContext())) {
            url = "http://" + this.localProvider.getLocalIP() + ":" + this.localProvider.getServletPort() + "/rpc/call/"
                    + this.className + "/" + methodName;
        } else {
            url = "http://" + this.localProvider.getLocalIP() + ":" + this.localProvider.getServletPort() + "/"
                    + localProvider.getServletContext() + "/rpc/call/" + this.className + "/" + methodName;
        }

        String[] methodDesc = new String[4];
        methodDesc[0] = method.getName();
        methodDesc[1] = getParameters(method);
        methodDesc[2] = url;
        StringBuilder methodDetail = new StringBuilder(methodDesc[0] + "(");
        Type[] paramTypes = method.getGenericParameterTypes();
        if (paramTypes.length > 0) {
            for (Type type : paramTypes) {
                String typeString = type.toString();
                if (typeString.startsWith("class ")) {
                    typeString = typeString.replace("class ", "");
                }
                methodDetail.append(typeString).append(",");
            }
            int lastIndexOf = methodDetail.lastIndexOf(",");
            methodDetail = new StringBuilder(methodDetail.substring(0, lastIndexOf));
        }
        methodDetail.append(")");
        methodDesc[3] = methodDetail.toString();

        restfullToMethodDesc.put(methodName, methodDesc);
    }

    private String getParameters(Method method) {
        StringBuilder additionalName = new StringBuilder();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            additionalName = new StringBuilder("NA");
        } else {
            for (Class<?> class1 : parameterTypes) {
                additionalName.append("_").append(class1.getSimpleName());
            }
        }
        return additionalName.toString();
    }

    public Map<String, Method> getRestfullToMethod() {
        return restfullToMethod;
    }

    public Map<String, String[]> getRestfullToMethodDesc() {
        return restfullToMethodDesc;
    }
}
