package hu.exclusive.crm.businesslogic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.CafeteriaInfo;
import hu.exclusive.dao.model.CafeteriaUtil;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.StaffBase;
import hu.exclusive.dao.model.StaffCafeteria;

/**
 * A cafeteria számításokhoz használt logika. Egyrészt az excel összeállításban
 * a bevitt összegeket ellenőrzi. Másrészt a munkaidőben szabályként képes a
 * fizetés nélküli szabik alapján az éves keretet korrigálni.
 * <ul>
 * 
 * <li>A cafetéria éves összege a munkakezdés éveitől, a havi naptól, a napi
 * óráktól függ. Ezt nem haladhatja meg az havi összes.
 * 
 * <li>A kategóriákban is lehetnek limitek. Éves és havi is. Ezt sem kéne
 * túllépni.
 * 
 * <li>Minden hónapban 5-ig az aktuális, később a megelőző feltételek igazak. Ez
 * jelentheto azt, hogy még nem cafetéria jogosult, mert még nem érte el az
 * évet, de azt is, hogy adott hónapban a kisebb éves összeg él, mert közben
 * lépi át az éveket.
 * 
 * <li>A GYES féle szabikat a munkatárs passzív státusza jelzi. Ezek után ha
 * egyből szabira megy, amit a gyes alatt halmozott fel, akkor az nem cafe alap.
 * 
 * <li>A napi órák alapján készül el az excel éve elején, a munkatárs kérte
 * kategóriák között szétosztva.
 * 
 * <li>Ezt időszakosan, havonta korrigálják az összegre nézve. ha fizetés
 * nélkülin szabin volt, akkor azok a napok csökkentik az éves keretet. Erről
 * email is készül. A szabikat a munkaidőben klikkelik be a cellákba, és ott
 * lesz levonva az összeg is. Mivel az elosztás eléggé önkényes, így csak az
 * éves korrigálódik, az átvezetése már manuális amira a mail hívja fel a
 * figyelmet.
 * 
 * <li>Ha a amunkatárs adatlapján a heti óra változik, akkor is összeg korrekció
 * és figyelmeztető levél.
 * 
 * <li>A a munkaidőben 14 napnál hosszabban beteg, vagy táppénzes, akkor is
 * összeg korrekció és figyelmeztető levél.
 * 
 * <li>Ha korekciós, de nincs is cafetéria rekordja, az jelzi, hogy nem kell
 * foglalkozni vele, mert még "fiatal".
 * 
 * <li>Ha valaki kilépett lesz, akkor is megy levél.
 * 
 * @author PK
 *
 */
public class CafeteriaRule implements IWorktimeRule {

	private StaffCafeteria staff;
	private final java.util.Vector<String> problems = new java.util.Vector<>();
	private final java.util.Vector<String> errors = new java.util.Vector<>();
	private final CafeteriaUtil util = new CafeteriaUtil();
	private String msgprefix = "";
	private final String BR = "<br/>";

	@Override
	public void init(StaffBase staff) {
		this.staff = (StaffCafeteria) staff;
		// msgprefix = staff.getFullName() + ": ";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean calculateRule(Object... params) {
		boolean ok = true;
		int year = (int) params[0];
		util.setYear(year);
		util.setInfos((List<CafeteriaInfo>) params[1]);
		util.setMonths((List<Cafeteria>) params[2]);
		ok = checkYearlyLimit();
		ok = checkCategoriesLimit() && ok;
		return ok;
	}

	/**
	 * Az éves felhasználható limiten belül maradjn a havik összege. Ha több
	 * error, ha kevesebb problem.
	 * 
	 * @return
	 */
	private boolean checkYearlyLimit() {
		int yearly = util.getCurrentInfo().getYearLimit() == null ? 0 : util.getCurrentInfo().getYearLimit().intValue();
		int monthlies = 0;
		for (Cafeteria cafe : util.getYearlyCafes(util.getCurrentYear())) {
			monthlies += cafe.getAmount() == null ? 0 : cafe.getAmount().intValue();
		}
		if (yearly < monthlies) {
			errors.add(msgprefix + "A havi összegek (" + monthlies + ") meghaladják az éves keretet (" + yearly + ")!");
			return false;
		}

		if (yearly > monthlies) {
			problems.add(
					msgprefix + "A havi összegek (" + monthlies + ") nem töltik ki az éves keretet (" + yearly + ")!");
		}

		return true;
	}

	public void setCategories(List<PCafeteriaCategory> categories) {
		util.setCategories(categories);
	}

	/**
	 * A kategórák szummái vagy akár egy havi limitek ellenőrzése. Nem hatékony
	 * megoldás.
	 * 
	 * @return
	 */
	private boolean checkCategoriesLimit() {

		Map<Integer, Integer> catYearlySummas = new HashMap<>();
		Map<Integer, String> catYearlyErrors = new HashMap<>();
		boolean ok = true;

		for (Cafeteria cafe : util.getYearlyCafes(util.getCurrentYear())) {
			if (cafe.getAmount() != null) {

				int catId = cafe.getCafeCategory().getIdCafeteriaCat();
				int ylimit = util.getCategoryYearlyLimit(catId);
				if (ylimit > 0 && !catYearlyErrors.containsKey(catId)) {
					if (!catYearlySummas.containsKey(catId)) {
						catYearlySummas.put(catId, 0);
					}

					int l = catYearlySummas.get(catId) + cafe.getAmount().intValue();
					if (l > ylimit) {
						// elerte az eves limitet
						catYearlyErrors.put(catId, "A '" + cafe.getCafeCategory().getCategoryName()
								+ "' az éves limitet ('" + ylimit + "') túllépte!");
						ok = false;
					}
				}
				int mlimit = util.getCategoryMonthlyLimit(cafe.getCafeCategory().getIdCafeteriaCat());
				if (mlimit > 0) {
					int l = cafe.getAmount().intValue();
					if (l > mlimit) {
						// elerte az eves limitet
						errors.add(msgprefix + "A '" + cafe.getCafeCategory().getCategoryName() + "' "
								+ cafe.getMonthKey() + ". havi limitet (" + mlimit + ") túllépte!");
						ok = false;
					}
				}
			}
		}

		for (Integer key : catYearlyErrors.keySet()) {
			errors.add(msgprefix + catYearlyErrors.get(key));
		}

		return ok;

	}

	@Override
	public String infoMessage(String... additionalText) {
		if (additionalText != null && additionalText.length > 0) {
			problems.addAll(Arrays.asList(additionalText));
		}
		return problems.isEmpty() ? null : StringUtils.join(problems.toArray(), BR);
	}

	@Override
	public String errorMessage(String... additionalText) {
		if (additionalText != null && additionalText.length > 0) {
			errors.addAll(Arrays.asList(additionalText));
		}
		return errors.isEmpty() ? null : StringUtils.join(errors.toArray(), BR);
	}

	public CafeteriaUtil getUtil() {
		return this.util;
	}

}
