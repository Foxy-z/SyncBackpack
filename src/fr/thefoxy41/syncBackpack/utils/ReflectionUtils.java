package fr.thefoxy41.syncBackpack.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private static final String packageVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    private static final short intVersion = Short.parseShort(packageVersion.split("_")[1]);

    public static Class<?> getClass(String nmsClassName) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + packageVersion + ".entity." + nmsClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getSkullEnumConstant() {
        if (intVersion < 13) {
            return "SKULL_ITEM";
        }
        return "PLAYER_HEAD";
    }

    private static Class<?> wrapperToPrimitive(Class<?> clazz) {
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Integer.class) return int.class;
        if (clazz == Double.class) return double.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Short.class) return short.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Void.class) return void.class;
        if (clazz == Character.class) return char.class;
        return clazz;
    }

    private static Class<?>[] toParamTypes(Object... params) {
        Class[] classes = new Class[params.length];
        for (int i = 0; i < params.length; i++)
            classes[i] = wrapperToPrimitive(params[i].getClass());
        return classes;
    }

    public static Object callMethod(Object object, String method, Object... params) {
        try {
            Method m = object.getClass().getMethod(method, toParamTypes(params));
            m.setAccessible(true);
            return m.invoke(object, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getDeclaredField(Object object, String field) {
        try {
            Field f = object.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
