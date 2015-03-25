package hu.exclusive.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class ObjectUtils {

    public final static SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy.MM.dd");

    public static Object nv(Object o) {
        if (o != null && o instanceof String) {
            if (((String) o).isEmpty())
                return null;
        }
        return o;
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static boolean isCollection(Object obj) {
        return obj != null && (obj instanceof Collection<?>);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> toCollection(Object objects, Class<T> itemType) {
        Collection<T> collection = new ArrayList<T>();
        if (isCollection(objects)) {
            collection = (Collection<T>) objects;
        } else if (isArray(objects)) {
            for (T t : (T[]) objects) {
                collection.add(t);
            }
        }
        return collection;
    }

    public static String toString(Object anyvalues) {
        try {
            if (nv(anyvalues) == null)
                return "null";
            if (isArray(anyvalues) || isCollection(anyvalues)) {
                Collection<?> coll = toCollection(anyvalues, Object.class);
                if (coll.isEmpty())
                    return "[]";
                return Arrays.toString(coll.toArray(new Object[0]));
            }
            if (anyvalues instanceof Date)
                return SDF_YYYYMMDD.format((Date) anyvalues);

            return String.valueOf(anyvalues);

        } catch (Exception e) {
            return e.getClass().getName() + ":" + e.getLocalizedMessage();
        }

    }

    public static void main(String[] args) {
        try {
            Date[] dt = { new Date(), new Date() };
            System.out.println(toCollection(dt, Object.class));
            System.out.println(new SimpleDateFormat("yyyy").parse("3000").getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
