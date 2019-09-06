package de.sopra.javagame.util;

import de.sopra.javagame.model.Copyable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 06.09.2019
 * @since 06.09.2019
 */
public class CopyUtil {

    public static String copy(String string) {
        return new String(string.toCharArray());
    }

    public static <C, T extends Copyable<T>> C copy(Collection<T> collection, Collector<T, ?, C> collector) {
        return collection.stream().map(item -> item.copy()).collect(collector);
    }

    public static <T extends Copyable<T>> List<T> copyAsList(Collection<T> collection) {
        return copy(collection, Collectors.toList());
    }

    public static <T extends Copyable<T>> Set<T> copyAsSet(Collection<T> collection) {
        return copy(collection, Collectors.toSet());
    }

    public static <T extends Copyable<T>> void copyArr(T[] array, T[] target) {
        for (int i = 0; i < array.length; i++) {
            target[i] = array[i].copy();
        }
    }

    public static <T extends Copyable<T>> void copyArr(T[][] array, T[][] target) {
        for (int i = 0; i < array.length; i++) {
            T[] targetPart = target[i];
            T[] arrayPart = array[i];
            copyArr(arrayPart, targetPart);
            target[i] = targetPart;
        }
    }

}