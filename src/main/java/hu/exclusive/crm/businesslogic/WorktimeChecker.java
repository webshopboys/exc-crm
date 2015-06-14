package hu.exclusive.crm.businesslogic;

import java.util.ArrayList;
import java.util.List;

/**
 * A munkaidő szervezésben minden napra klikkelve ellőnrzések történnek, melyek alapján hibaüzenetek érkezhetnek. További extra
 * műveletek indikálása is lehetséges. Lényege, hogy a klikkelésre történik valami.
 * 
 * @author PK
 *
 */
public class WorktimeChecker {

    List<IWorktimeRule> rules = new ArrayList<IWorktimeRule>();

    public WorktimeChecker() {
        this.rules.add(new DailyWorkplaceRule());
        this.rules.add(new DailyWorktimeRule());
        this.rules.add(new MonthlyWorkplaceRule());
        this.rules.add(new MonthlyWorktimeRule());
        this.rules.add(new CafeteriaCalculator());
    }

    public void onClickCell() {

    }
}
