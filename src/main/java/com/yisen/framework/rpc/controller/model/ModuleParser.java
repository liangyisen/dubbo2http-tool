package com.yisen.framework.rpc.controller.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@SuppressWarnings("rawtypes")
public class ModuleParser {

    public Class getActualClass(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedFieldType = (ParameterizedType) type;
            if ("sun.reflect.generics.reflectiveObjects.TypeVariableImpl"
                    .equals(parameterizedFieldType.getActualTypeArguments()[0].getClass().getName())) {
                return Object.class;
            } else {
                return getActualClass(parameterizedFieldType.getActualTypeArguments()[0]);
            }
        } else {
            return (Class) type;
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<Object> handleJSONArray(List<Object> objectArray, Class actualType) {
        List<Object> list = new ArrayList<>();
        for (Object object : objectArray) {
            HashMap map = new HashMap<String, Object>();
            if (object instanceof List) {
                list.add(handleJSONArray((List) object, actualType));
            } else {
                if (object instanceof Map) {
                    JSONObject jObj = new JSONObject((Map) object);
                    list.add(JSON.parseObject(jObj.toJSONString(), actualType));
                } else {
                    list.add(JSON.parseObject("" + object, actualType));
                }
            }
            list.add(map);
        }
        return list;
    }

    public Object initObj(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (this.isInitableField(obj.getClass(), field.getName())) {
                try {
                    initField(obj, field);
                } catch (IntrospectionException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public Object initMethodParam(Method method, int paramIndex) {
        Class<?> paramClass = method.getParameterTypes()[paramIndex];
        Object obj2bSet = null;
        if (paramClass.isInterface()) {
            Type actualType = method.getGenericParameterTypes()[paramIndex];
            if (List.class.isAssignableFrom(paramClass)) {
                Class actualClass = this.getActualClass(actualType);
                List list = new ArrayList<>();
                list.add(initObj(this.constructTheClass(actualClass)));
                obj2bSet = list;
            } else if (Set.class.isAssignableFrom(paramClass)) {
                Class actualClass = this.getActualClass(actualType);
                Set set = new HashSet();
                set.add(initObj(this.constructTheClass(actualClass)));
                obj2bSet = set;
            } else if (Map.class.isAssignableFrom(paramClass)) {
                ParameterizedType parameterizedFieldType = (ParameterizedType) actualType;
                Class keyClaz = (Class) parameterizedFieldType.getActualTypeArguments()[0];
                Class valueClaz = (Class) parameterizedFieldType.getActualTypeArguments()[1];
                Map map = new HashMap<>();
                map.put(initObj(this.constructTheClass(keyClaz)), initObj(this.constructTheClass(valueClaz)));
                obj2bSet = map;
            }
        } else {
            obj2bSet = this.constructTheClass(paramClass);
            obj2bSet = initObj(obj2bSet);
        }
        return obj2bSet;
    }

    private String getActualClassName(String className) {
        switch (className) {
            case "char":
                return Character.class.getName();
            case "boolean":
                return Boolean.class.getName();
            case "byte":
                return Byte.class.getName();
            case "short":
                return Short.class.getName();
            case "int":
                return Integer.class.getName();
            case "long":
                return Long.class.getName();
            case "float":
                return Float.class.getName();
            case "double":
                return Double.class.getName();
            default:
                return className;
        }
    }

    @SuppressWarnings("unchecked")
    public void initField(Object obj, Field field) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            String name = getActualClassName(field.getType().getName());
            Object obj2bSet = null;
            Class claz = Class.forName(name);
            Method writter = this.getWritter(obj.getClass(), field.getName());

            if (claz.isInterface()) {
                Type actualType = writter.getGenericParameterTypes()[0];
                if (List.class.isAssignableFrom(claz)) {
                    Class actualClass = this.getActualClass(actualType);
                    List list = new ArrayList<>();
                    list.add(initObj(this.constructTheClass(actualClass)));
                    obj2bSet = list;
                } else if (Set.class.isAssignableFrom(claz)) {
                    Class actualClass = this.getActualClass(actualType);
                    Set set = new HashSet();
                    set.add(initObj(this.constructTheClass(actualClass)));
                    obj2bSet = set;

                } else if (Map.class.isAssignableFrom(claz)) {
                    ParameterizedType parameterizedFieldType = (ParameterizedType) actualType;
                    Class keyClaz = (Class) parameterizedFieldType.getActualTypeArguments()[0];
                    Class valueClaz = (Class) parameterizedFieldType.getActualTypeArguments()[1];
                    Map map = new HashMap<>();
                    map.put(initObj(this.constructTheClass(keyClaz)), initObj(this.constructTheClass(valueClaz)));
                    obj2bSet = map;
                }
            } else if (claz.isArray()) {
                Class actualTypeName = claz.getComponentType();
                obj2bSet = Array.newInstance(actualTypeName, 1);
                Array.set(obj2bSet, 0, initObj(this.constructTheClass(actualTypeName)));
            } else {
                obj2bSet = this.constructTheClass(claz);
                obj2bSet = initObj(obj2bSet);
            }
            writter.invoke(obj, obj2bSet);
        } catch (ClassNotFoundException ex) {
        }
    }

    @SuppressWarnings("unchecked")
    public Object constructTheClass(Class claz) {
        try {
            claz = Class.forName(this.getActualClassName(claz.getName()));
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        Object obj2bSet = null;
        if (claz == Integer.class) {
            obj2bSet = 0;
        } else if (claz == Float.class) {
            obj2bSet = 0f;
        } else if (claz == Double.class) {
            obj2bSet = 0.0;
        } else if (claz == Boolean.class) {
            obj2bSet = true;
        } else if (claz == Short.class) {
            obj2bSet = 0;
        } else if (claz == Long.class) {
            obj2bSet = 0L;
        } else if (claz == Character.class) {
            obj2bSet = 'a';
        } else if (claz == String.class) {
            obj2bSet = "string";
        } else if (claz == Byte.class) {
            obj2bSet = 0;
        } else if (claz == BigDecimal.class) {
            obj2bSet = BigDecimal.valueOf(66666.666);
        } else if (claz == LocalDateTime.class) {
            obj2bSet = LocalDateTime.now();
        } else if (claz == LocalTime.class) {
            obj2bSet = LocalTime.now();
        } else if (claz.isEnum()) {
            Method method;
            try {
                method = claz.getMethod("values");
                Object inter[] = (Object[]) method.invoke(null, new Object[]{});
                obj2bSet = inter[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                int conLength = claz.getConstructors().length;
                if (0 != conLength) {
                    Class[] constructorParams = {};
                    if (claz.getConstructor(constructorParams) != null) {
                        obj2bSet = claz.newInstance();
                    } else {
                        constructorParams = new Class[]{String.class};
                        Constructor constructor = claz.getConstructor(constructorParams);
                        if (constructor != null) {
                            obj2bSet = constructor.newInstance("0");
                        } else {
                            obj2bSet = claz.getConstructor(int.class).newInstance(0);
                        }
                    }
                } else {
                    obj2bSet = claz.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj2bSet;
    }

    private boolean isInitableField(Class clazz, String fieldName) {
        Method writeMethod = null;
        try {
            writeMethod = this.getWritter(clazz, fieldName);
        } catch (IntrospectionException e) {
        }
        if (writeMethod == null) {
            return false;
        } else {
            return true;
        }
    }

    private Method getWritter(Class clazz, String fieldName) throws IntrospectionException {
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
        return pd.getWriteMethod();
    }
}
