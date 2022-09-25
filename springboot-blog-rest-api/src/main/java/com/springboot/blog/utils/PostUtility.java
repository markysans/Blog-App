package com.springboot.blog.utils;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

public class PostUtility {
    public static Object fieldMapper(Object source, Object destination) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PropertyUtils.describe(source).entrySet().stream()
                .filter(e -> e.getValue() != null)
                .filter(e -> !e.getKey().equals("class"))
                .forEach(e -> {
                    try{
                        PropertyUtils.setProperty(destination, e.getKey(), e.getValue());
                    } catch(Exception exp){
                        System.out.println(exp);
                    }
                });
        return destination;
    }
}
