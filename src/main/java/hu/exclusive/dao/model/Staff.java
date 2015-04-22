package hu.exclusive.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_STAFF")
public class Staff extends StaffBase {

    private static final long serialVersionUID = 3945633186968471433L;

    public static enum DOCFIELDS_NAMES {
        TELJESNEV, SZULETESINEV, SZULETESIDATUM, ALLANDOCIM, SZULETESIHELY, MUNKAKEZDES, ANYJANEVE, salaryType, SZIGSZAM, TAJSZAM, ADOSZAM;

        public String getLabel() {
            switch (this) {
            case TELJESNEV:
                return "Teljes név";
            case SZULETESINEV:
                return "Születése név";
            case SZULETESIDATUM:
                return "Születései dátum";
            case SZULETESIHELY:
                return "Születési hely";
            case ANYJANEVE:
                return "Anyja neve";
            case SZIGSZAM:
                return "Okirat száma";
            case TAJSZAM:
                return "TAJ szám";
            case ADOSZAM:
                return "Adószám";
            case ALLANDOCIM:
                return "Állandó cím";
            case MUNKAKEZDES:
                return "Munkába állás dátuma";
            default:
                return "-";
            }
        }

        public static String getInfo() {
            StringBuilder sb = new StringBuilder();
            for (DOCFIELDS_NAMES field : DOCFIELDS_NAMES.values()) {
                sb.append(field.name()).append(" (" + field.getLabel() + "); ");
            }

            return sb.toString();
        }
    };

}
