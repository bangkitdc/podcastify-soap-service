package com.podcastify.utils;

import javax.jws.WebMethod;
import java.lang.reflect.Method;

public class MethodList {
    public static void printAvailableMethods(Class<?> serviceInterface) {
        System.out.println("Available methods:");
        for (Method method : serviceInterface.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebMethod.class)) {
                System.out.println("- " + method.getName());
            }
        }
    }

    public static void printProcessStatus(int code, String endpoint, String methodName) {
        System.out.println("[" + code +  "] Endpoint: " + endpoint + " --- Method: " + methodName);
    }
}