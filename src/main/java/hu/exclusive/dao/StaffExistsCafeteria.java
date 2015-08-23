package hu.exclusive.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.utils.ObjectUtils;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó T_CAFETERIA és esetleg később a
 * T_CAFETERIA_INFO alapján. A cafeteria tábláknak nincs külön kulcstáblája,
 * önmaga a kulcstábla. Az ahol a staffhoz egyezik a dátum hónapig és a
 * kategória. <br>
 * Paraméterek a item.values Map kulcsaikent: <b>"yearKey",
 * "monthKey","idCafeteriaCat"</b>
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsCafeteria implements IExitsPredicator {

	@Override
	public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
			CriteriaQuery<?> query, Root<?> baseRoot) {

		// select STAFF S WHERE exists (SELECT CAFETERIA C where C.SID = S.SID
		// and C.???)
		Subquery<Cafeteria> cafeJoin = query.subquery(Cafeteria.class);
		Root<Cafeteria> cafe = cafeJoin.from(Cafeteria.class);
		cafeJoin.select(cafe);

		Predicate joinStaff = builder.equal(baseRoot.get("idStaff"), cafe.get("idStaff"));

		if ("yearKey".equals(item.getFieldName())) {

			Object yearKey = item.getValues();
			if (yearKey == null) {
				cafeJoin.where(joinStaff);
			} else {
				Predicate yearIs = builder.equal(cafe.get("yearKey"), yearKey);
				cafeJoin.where(builder.and(joinStaff, yearIs));
			}

		} else if ("monthKey".equals(item.getFieldName())) {

			Object monthKey = item.getValues();
			if (monthKey == null) {
				cafeJoin.where(joinStaff);
			} else {
				Predicate monthIs = builder.equal(cafe.get("monthKey"), monthKey);
				cafeJoin.where(builder.and(joinStaff, monthIs));
			}

		} else if ("idCafeteriaCat".equals(item.getFieldName())) {

			Object cafeCategory = item.getValues();
			if (cafeCategory == null) {
				cafeJoin.where(joinStaff);
			} else {
				Predicate categoryIn = cafe.get("cafeCategory").get("idCafeteriaCat")
						.in(ObjectUtils.toCollection(cafeCategory, Integer.class));
				cafeJoin.where(builder.and(joinStaff, categoryIn));
			}

		}

		filters.add(builder.exists(cafeJoin));
	}

}
