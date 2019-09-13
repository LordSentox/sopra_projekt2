package de.sopra.javagame;

import java.lang.reflect.Field;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 06.09.2019
 * @since 06.09.2019
 */
public class TestHelper {

    public static <T, C> T getAttribute(String fieldName, Class<C> fromClass, Class<T> typeCast, C instance) {
        try {
            Field declaredField = fromClass.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            Object content = declaredField.get(instance);
            declaredField.setAccessible(accessible);
            return (T) content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T, C> void setAttribute(String fieldName, Class<C> fromClass, C instance, T value) {
        try {
            Field declaredField = fromClass.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            declaredField.set(instance, value);
            declaredField.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}