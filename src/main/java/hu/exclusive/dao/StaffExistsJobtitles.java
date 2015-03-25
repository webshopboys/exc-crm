package hu.exclusive.dao;

import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.StaffJobtitleK;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó ALARM alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsJobtitles implements IExitsPredicator {

    @SuppressWarnings("unchecked")
    @Override
    public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {

        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<Jobtitle> jobtitleJoin = query.subquery(Jobtitle.class);
        Root<Jobtitle> jobtitle = jobtitleJoin.from(Jobtitle.class);
        Root<StaffJobtitleK> jobtitleKey = jobtitleJoin.from(StaffJobtitleK.class);
        jobtitleJoin.select(jobtitle);

        Predicate join1 = builder.equal(jobtitle.get("idJobtitle"), jobtitleKey.get("idJobtitle"));
        Predicate join2 = builder.equal(baseRoot.get("idStaff"), jobtitleKey.get("idStaff"));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = jobtitle.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();
            // Predicate p5 = builder.like(jobtitle.<String> get("jobtitle"), "%");

            Predicate search1 = item.createPredicate(builder, field, relation, value);

            jobtitleJoin.where(builder.and(join1, join2, search1));

        }

        filters.add(builder.exists(jobtitleJoin));
    }
}
