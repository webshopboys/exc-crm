package hu.exclusive.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import hu.exclusive.dao.model.StaffWorkgroupK;
import hu.exclusive.dao.model.Workgroup;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó WORKGROUP alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsWorkgroup implements IExitsPredicator {

	@Override
	public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
			CriteriaQuery<?> query, Root<?> baseRoot) {

		Subquery<Workgroup> workgroupJoin = query.subquery(Workgroup.class);
		Root<Workgroup> workgroup = workgroupJoin.from(Workgroup.class);
		Root<StaffWorkgroupK> jobtitleKey = workgroupJoin.from(StaffWorkgroupK.class);
		workgroupJoin.select(workgroup);

		Predicate joinCriteria1 = builder.equal(workgroup.get("idWorkgroup"), jobtitleKey.get("id").get("idWorkgroup"));
		Predicate joinCriteria2 = builder.equal(baseRoot.get("idStaff"), jobtitleKey.get("id").get("idStaff"));

		{
			// Itt lehetne több kereső mezőt is kezelni és akkor több px jönne
			// még hozzá (pl: több mezőben like)
			final Path<?> field = workgroup.get(item.getFieldName());

			final DaoFilter.RELATION relation = item.getRelation();

			Object value = item.getValues();
			// Predicate p5 = cb.like(workgroup.<String> get("groupName"), "%");
			Predicate searchCriteria1 = item.createPredicate(builder, field, relation, value);

			workgroupJoin.where(builder.and(joinCriteria1, joinCriteria2, searchCriteria1));
		}

		filters.add(builder.exists(workgroupJoin));
	}
}
