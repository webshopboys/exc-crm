package hu.exclusive.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Az entityk segedmuveleteihez hasznalhato funkciok.
 * 
 * @author Petnehazi
 *
 */
public abstract class EntityCommons {

    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    protected String getDateTime(Date date) {
        if (date != null) {
            try {
                return DATETIME.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "-";
    }

    protected String getFileName(String path) {
        if (path != null) {
            try {
                if (path.lastIndexOf("/") > 0) {
                    return path.substring(path.lastIndexOf("/") + 1);
                } else if (path.lastIndexOf("\\") > 0) {
                    return path.substring(path.lastIndexOf("\\") + 1);
                }
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "[nincs megadva]";
    }

    protected boolean isEmpty(Object o) {
        if (o != null) {
            if (o instanceof String && ((String) o).trim().length() == 0)
                return true;
            return false;
        }
        return true;
    }

    public abstract Integer getId();
}
