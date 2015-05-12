package hu.exclusive.utils;

import hu.exclusive.dao.model.EntityCommons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ObjectUtils {

    public final static SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public static byte[] zip(byte[] bin, String entryName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        ZipEntry entry = new ZipEntry(entryName);
        entry.setSize(bin.length);
        zos.putNextEntry(entry);
        zos.write(bin);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }

    public static byte[] unzip(byte[] bin) throws FileNotFoundException, IOException {
        Map<String, byte[]> tuple = unzip2(bin);
        String key = tuple.keySet().iterator().next();
        return tuple.getOrDefault(key, new byte[0]);
    }

    public static Map<String, byte[]> unzip2(byte[] bin) throws FileNotFoundException, IOException {
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(bin));
        ZipEntry entry = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String entryName = "-";

        while ((entry = zipStream.getNextEntry()) != null) {

            entryName = entry.getName();

            byte[] byteBuff = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = zipStream.read(byteBuff)) != -1) {
                out.write(byteBuff, 0, bytesRead);
            }

            out.close();
            zipStream.closeEntry();
        }
        zipStream.close();

        Map<String, byte[]> tuple = new HashMap<String, byte[]>();
        tuple.put(entryName, out.toByteArray());

        return tuple;
    }

    public static boolean isZippedStream(InputStream stream) {
        boolean isZipped = false;
        try {
            if (stream != null)
                isZipped = new ZipInputStream(stream).getNextEntry() != null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isZipped;
    }

    public static byte[] serializeFile(String fileName, InputStream in) throws IOException {
        try {

            // write the inputStream to a FileOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            return out.toByteArray();

        } catch (IOException e) {
            throw e;
        }
    }

    public static String getDateTime(Date date) {
        if (date != null) {
            try {
                return SDF_DATETIME.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "-";
    }

    public static String getDate(Date date) {
        if (date != null) {
            try {
                return SDF_YYYYMMDD.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "-";
    }

    public static <T> T getFromList(List<? extends T> list, int id) {
        for (T t : list) {
            if (t instanceof EntityCommons && ((EntityCommons) t).getId() == id) {
                return t;
            }
        }
        return null;
    }

    public static int[] toIdArray(List<? extends EntityCommons> list) {

        if (list == null || list.isEmpty())
            return new int[0];

        int[] ids = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getId();
        }

        return ids;
    }

    public static String getTagX_String(String separatedMultiHolder, int splittedIndex) {
        return getTagX_String(separatedMultiHolder, splittedIndex, "_", null);
    }

    public static String getTagX_String(String separatedMultiHolder, int splittedIndex, String separator, String defaulValue) {
        try {
            if (separatedMultiHolder != null && separatedMultiHolder.contains(separator)) {

                String splittedString = separatedMultiHolder.split(separator)[splittedIndex];
                return isEmpty(splittedString) ? defaulValue : (splittedString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaulValue;
    }

    public static Double getTagX_Double(String separatedMultiHolder, int splittedIndex) {
        return getTagX_Double(separatedMultiHolder, splittedIndex, "_", null);
    }

    public static Double getTagX_Double(String separatedMultiHolder, int splittedIndex, String separator, Double defaulValue) {
        try {
            if (separatedMultiHolder != null && separatedMultiHolder.contains(separator)) {
                String splittedString = separatedMultiHolder.split(separator)[splittedIndex];
                splittedString = splittedString.replace(',', '.');
                return isEmpty(splittedString) ? defaulValue : Double.valueOf(splittedString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaulValue;
    }

    public static Integer getTagX_Integer(String separatedMultiHolder, int splittedIndex) {
        return getTagX_Integer(separatedMultiHolder, splittedIndex, "_", null);
    }

    public static Integer getTagX_Integer(String separatedMultiHolder, int splittedIndex, String separator, Integer defaulValue) {
        try {
            if (separatedMultiHolder != null && separatedMultiHolder.contains(separator)) {
                String splittedString = separatedMultiHolder.split(separator)[splittedIndex];
                splittedString = splittedString.replaceAll(" ", "");
                return isEmpty(splittedString) ? defaulValue : Integer.valueOf(splittedString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaulValue;
    }

    public static boolean getTagX_Boolean(String separatedMultiHolder, int splittedIndex) {
        return getTagX_Boolean(separatedMultiHolder, splittedIndex, "_", false);
    }

    public static boolean getTagX_Boolean(String separatedMultiHolder, int splittedIndex, String separator, boolean defaulValue) {
        try {
            if (separatedMultiHolder != null && separatedMultiHolder.contains(separator)) {
                String splittedString = separatedMultiHolder.split(separator)[splittedIndex];
                return isEmpty(splittedString) ? defaulValue : Boolean.getBoolean(splittedString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaulValue;
    }

    public static boolean isEmpty(Object o) {
        if (o != null) {
            if (o instanceof String) {
                return ((String) o).trim().length() == 0;
            }
            return false;
        }
        return true;
    }

    public static String getFileName(String path) {
        if (isEmpty(path))
            return "";
        int lastper = path.lastIndexOf("/");
        if (lastper > -1) {
            return path.substring(lastper + 1);
        }
        return path;
    }

    public static String getFilePath(String path) {
        if (isEmpty(path))
            return "";
        int lastper = path.lastIndexOf("/");
        if (lastper > -1) {
            return path.substring(0, lastper + 1);
        }
        return path;
    }

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
            // Date[] dt = { new Date(), new Date() };
            // System.out.println(toCollection(dt, Object.class));
            // System.out.println(new SimpleDateFormat("yyyy").parse("3000").getTime());

            String p = "c:/a/b/c/abc.txt";
            System.out.println(getFilePath(p));
            System.out.println(getFileName(p));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Megprobal valid fajlnevet kepezni.
     * 
     * @param dirtyName
     * @return
     */
    public static String cleanFilename(String dirtyName) {

        try {
            if (dirtyName != null) {
                return dirtyName.replace('?', '_').replace('*', '_').replace('/', '.').replace('\\', '.').replace('ő', 'o')
                        .replace('ű', 'u').replace('ú', 'u').replace('í', 'i').replace('Ő', 'O').replace('Ű', 'U')
                        .replace('Ú', 'U').replace('Í', 'I');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
