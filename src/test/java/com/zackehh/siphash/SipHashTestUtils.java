package com.zackehh.siphash;

import java.lang.reflect.Field;

public class SipHashTestUtils {

    static <T> T getPrivateField(Object obj, String name, Class<T> clazz) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return clazz.cast(f.get(obj));
    }

}
