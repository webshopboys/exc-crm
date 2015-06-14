package hu.exclusive.dao;

import hu.exclusive.dao.model.Cafeteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó T_CAFETERIA és esetleg később a T_CAFETERIA_INFO alapján.
 * A cafeteria tábláknak nincs külön kulcstáblájka, önmaga a kulcstábla.
 * Az ahol a staffhoz egyezik a dátum hónapig és a kategória. <br>
 * Paraméterek a item.values Map kulcsaikent: <b>"yearKey", "monthKey","idCafeteriaCat"</b>
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsCafeteria implements IExitsPredicator {

    @Override
    public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {

        Subquery<Cafeteria> cafeJoin = query.subquery(Cafeteria.class);
        Root<Cafeteria> cafe = cafeJoin.from(Cafeteria.class);
        cafeJoin.select(cafe);

        Predicate join1 = builder.equal(baseRoot.get("idStaff"), cafe.get("idStaff"));

        if ("yearKey".equals(item.getFieldName())) {

            Object yearKey = item.getValues();
            Predicate searchCriteria1 = yearKey == null ? builder.isNotNull(cafe.get("idStaff")) : builder.equal(
                    cafe.get("yearKey"), yearKey);
            cafeJoin.where(builder.and(join1, searchCriteria1));

        } else if ("monthKey".equals(item.getFieldName())) {

            Object monthKey = item.getValues();
            Predicate searchCriteria2 = monthKey == null ? builder.isNotNull(cafe.get("idStaff")) : builder.equal(
                    baseRoot.get("monthKey"), monthKey);
            cafeJoin.where(builder.and(join1, searchCriteria2));

        } else if ("idCafeteriaCat".equals(item.getFieldName())) {

            Object cafeCategory = item.getValues();
            Predicate searchCriteria3 = cafeCategory == null ? builder.isNotNull(cafe.get("idStaff")) : builder.equal(
                    baseRoot.get("cafeCategory.idCafeteriaCat"), cafeCategory);
            cafeJoin.where(builder.and(join1, searchCriteria3));

        }

        filters.add(builder.exists(cafeJoin));
    }
}
