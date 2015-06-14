package hu.exclusive.crm.businesslogic;

/**
 * A cafeteria számításokhoz használt logika. Egyrészt az excel összeállításban a bevitt összegeket ellenőrzi. Másrészt a
 * munkaidőben szabályként képes a fizetés nélküli szabik alapján az éves keretet korrigálni.
 * <ul>
 * <li>A cafetéria éves összege a munkakezdés éveitől, a havi naptól, a napi óráktól függ.
 * <li>Minden hónapban 5-ig az aktuális, később a megelőző feltételek igazak. Ez jelentheto azt, hogy még nem cafetéria jogosult,
 * mert még nem érte el az évet, de azt is, hogy adott hónapban a kisebb éves összeg él, mert közben lépi át az éveket.
 * <li>A GYES féle szabikat a munkatárs passzív státusza jelzi. Ezek után ha egyből szabira megy, amit a gyes alatt halmozott fel,
 * akkor az nem cafe alap.
 * <li>A napi órák alapján készül el az excel éve elején, a munkatárs kérte kategóriák között szétosztva.
 * <li>Ezt időszakosan, havonta korrigálják az összegre nézve. ha fizetés nélkülin szabin volt, akkor azok a napok csökkentik az
 * éves keretet. Erről email is készül. A szabikat a munkaidőben klikkelik be a cellákba, és ott lesz levonva az összeg is. Mivel
 * az elosztás eléggé önkényes, így csak az éves korrigálódik, az átvezetése már manuális amira a mail hívja fel a figyelmet.
 * <li>Ha a amunkatárs adatlapján a heti óra változik, akkor is összeg korrekció és figyelmeztető levél.
 * <li>A a munkaidőben 14 napnál hosszabban beteg, vagy táppénzes, akkor is összeg korrekció és figyelmeztető levél.
 * <li>Ha korekciós, de nincs is cafetéria rekordja, az jelzi, hogy nem kell foglalkozni vele, mert még "fiatal".
 * <li>Ha valaki kilépett lesz, akkor is megy levél.
 * 
 * @author PK
 *
 */
public class CafeteriaCalculator implements IWorktimeRule {

    @Override
    public void init(Object requiredBeans) {
        // TODO Auto-generated method stub

    }

    @Override
    public void calculateRule(Object requiredBeans) {
        // TODO Auto-generated method stub

    }

    @Override
    public String infoMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String errorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
