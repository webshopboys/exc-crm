package hu.exclusive.crm.businesslogic;

import hu.exclusive.dao.model.StaffBase;

/**
 * Mindenféle ellenőrzési szabályok és műveletek általános interfésze. A
 * paraméter object-ek mindig egyediek, elhagyhatók és a szükséges segéd infókat
 * hivatottak átvinni.
 * 
 * @author PK
 *
 */
public interface IWorktimeRule {

	void init(StaffBase staff);

	boolean calculateRule(Object... params);

	String infoMessage(String... additionalText);

	String errorMessage(String... additionalText);

}
