package hu.exclusive.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A filter szuréséhez a kapcsolt táblákra exists subselect rész képző osztályok
 * felülete.
 * 
 * @author Petnehazi
 *
 */
public interface IExitsPredicator {

	/**
	 * A baseRoot <?>(pl. STAFF). Ehhez egy exists pl. egy kulcstablan at. Ezzel
	 * osszekotjuk, a megfelelo subselect-tel parameterezuk es exist-sel
	 * bekotjuk. Pl. STAFF - K_STAFF_ALARM - ALARM: select STAFF S where exists
	 * (select 1 from K_STAFF_ALARM K join ALARM A where A.AID = K.AID and S.SID
	 * = K.SID and ...)
	 * 
	 * @param filters
	 * @param item
	 * @param builder
	 * @param query
	 * @param baseRoot
	 */
	void preperaPredicate(List<Predicate> filters, DaoFilter item, CriteriaBuilder builder, CriteriaQuery<?> query,
			Root<?> baseRoot);
}
