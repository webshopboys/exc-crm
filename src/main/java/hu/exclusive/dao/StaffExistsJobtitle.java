package hu.exclusive.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.StaffJobtitleK;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó JOBTITLE alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsJobtitle implements IExitsPredicator {

	@Override
	public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
			CriteriaQuery<?> query, Root<?> baseRoot) {

		Subquery<Jobtitle> jobtitleJoin = query.subquery(Jobtitle.class);
		Root<Jobtitle> jobtitle = jobtitleJoin.from(Jobtitle.class);
		jobtitleJoin.select(jobtitle);

		Root<StaffJobtitleK> jobtitleKey = jobtitleJoin.from(StaffJobtitleK.class);

		Predicate join1 = builder.equal(jobtitle.get("idJobtitle"), jobtitleKey.get("idJobtitle"));
		Predicate join2 = builder.equal(baseRoot.get("idStaff"), jobtitleKey.get("idStaff"));

		{
			// Itt lehetne több kereső mezőt is kezelni és akkor több px jönne
			// még hozzá (pl: több mezőben like)
			final Path<?> field = jobtitle.get(item.getFieldName());

			final DaoFilter.RELATION relation = item.getRelation();

			Object value = item.getValues();
			// Predicate p5 = builder.like(jobtitle.<String> get("jobtitle"),
			// "%");

			Predicate search1 = item.createPredicate(builder, field, relation, value);

			jobtitleJoin.where(builder.and(join1, join2, search1));

		}

		filters.add(builder.exists(jobtitleJoin));
	}
}
