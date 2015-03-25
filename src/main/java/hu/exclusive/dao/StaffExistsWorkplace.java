package hu.exclusive.dao;

import hu.exclusive.dao.model.StaffWorkplaceK;
import hu.exclusive.dao.model.Workplace;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó WORKSPACE alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsWorkplace implements IExitsPredicator {

    @SuppressWarnings("unchecked")
    @Override
    public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {

        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<Workplace> workspaceJoin = query.subquery(Workplace.class);
        Root<Workplace> workspace = workspaceJoin.from(Workplace.class);
        Root<StaffWorkplaceK> jobtitleKey = workspaceJoin.from(StaffWorkplaceK.class);
        workspaceJoin.select(workspace);

        Predicate joinCriteria1 = builder.equal(workspace.get("idWorkplace"), jobtitleKey.get("id").get("idWorkplace"));
        Predicate joinCriteria2 = builder.equal(baseRoot.get("idStaff"), jobtitleKey.get("id").get("idStaff"));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = workspace.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();
            // Predicate p5 = cb.like(workgroup.<String> get("groupName"), "%");
            Predicate searchCriteria1 = item.createPredicate(builder, field, relation, value);

            workspaceJoin.where(builder.and(joinCriteria1, joinCriteria2, searchCriteria1));
        }

        filters.add(builder.exists(workspaceJoin));
    }
}
