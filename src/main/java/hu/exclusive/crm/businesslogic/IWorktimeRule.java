package hu.exclusive.crm.businesslogic;

/**
 * Mindenféle ellenőrzési szabályok és műveletek általános interfésze.
 * 
 * @author PK
 *
 */
public interface IWorktimeRule {

    void init(Object requiredBeans);

    void calculateRule(Object requiredBeans);

    String infoMessage();

    String errorMessage();

}
