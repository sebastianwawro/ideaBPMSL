package pl.edu.prz.stud.swawro.server.controller;

import java.lang.reflect.Method;

public class PacketRoute {
    private Class classRoute;
    private Method methodRoute;

    public PacketRoute(Class classRoute, String methodName) throws Exception {
        try {
            //classRoute = Class.forName(className);
            this.classRoute = classRoute;
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = String.class;
            methodRoute = classRoute.getMethod(methodName, parameterTypes);
        }
        catch (Exception e) {
            throw new Exception("Cannot create route");
        }
    }

    public String tryInvoke(String request) throws Exception {
        Object response = null;
        try {
           // classRoute.getDeclaredConstructors()[0].setAccessible(true);
            Object ob = classRoute.newInstance();
            Object[] arg = new Object[1];
            arg[0] = request;
            methodRoute.setAccessible(true);
            response = methodRoute.invoke(ob, arg);
            System.out.println(response);
            return (String)response;
        }
        catch (Exception e) {
            throw new Exception("Cannot invoke method");
        }
        finally {
            if (response != null) return (String)response;
            else return null;
        }
    }
}
