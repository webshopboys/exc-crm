package hu.exclusive.dao.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffDocument;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class ExcDaoServiceImpl implements IExcDaoService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Staff> getStaffList(DaoFilter filter) {

        // System.err.println("start staff listing " + new Date());

        // EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Staff> cq = cb.createQuery(Staff.class);

        Root<Staff> r = cq.from(Staff.class);

        Path<String> p = r.get("fullName");
        // cq.groupBy(p);
        // Path<Date> p = r.get("birthDate");
        cq.orderBy(cb.asc(p));

        List<Predicate> predicateList = filter.createPredicates(cb, cq, r);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);

        if (predicates.length == 1) {
            cq.where(predicates[0]);
        } else if (predicates.length > 1) {
            cq.where(filter.isAnyEnought() ? cb.or(predicates) : cb.and(predicates));
        }

        TypedQuery<Staff> tq = em.createQuery(cq);

        List<Staff> results = tq.setFirstResult(filter.getStartIndex()).setMaxResults(filter.getPageSize()).getResultList();

        Long count = getCountByCriteria(cq, cb, filter);

        filter.setTotalCount(count == null ? 0 : count);

        System.err.println("count " + count + " or " + filter.getTotalCount() + " per " + results.size());

        return results;
    }

    // private long getStaffListCount(DaoFilter filter) {
    // CriteriaBuilder cb = em.getCriteriaBuilder();
    // CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    //
    // Root<Staff> r = cq.from(Staff.class);
    // cq.select(cb.count(r));
    //
    // List<Predicate> predicateList = filter.createPredicates(cb, r);
    //
    // Predicate[] predicates = new Predicate[predicateList.size()];
    // predicateList.toArray(predicates);
    // cq.where(predicates);
    //
    // return em.createQuery(cq).getSingleResult();
    // }

    public <T> Long getCountByCriteria(CriteriaQuery<T> query, CriteriaBuilder builder, DaoFilter filter) {
        CriteriaQuery<Long> cqCount = builder.createQuery(Long.class);
        Root<?> entityRoot = cqCount.from(query.getResultType());

        cqCount.select(builder.count(entityRoot));

        List<Predicate> predicateList = filter.createPredicates(builder, cqCount, entityRoot);
        // System.err.println("predicateList " + predicateList);

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        if (predicates.length == 1) {
            cqCount.where(predicates[0]);
        } else if (predicates.length > 1) {
            cqCount.where(filter.isAnyEnought() ? builder.or(predicates) : builder.and(predicates));
        }

        return em.createQuery(cqCount).getSingleResult();
    }

    @Override
    public List<Jobtitle> getJobtitles(DaoFilter filter) {

        // CriteriaBuilder builder = em.getCriteriaBuilder();
        // CriteriaQuery<Jobtitle> query = builder.createQuery(Jobtitle.class);
        // Root<Jobtitle> root = query.from(Jobtitle.class);
        // query.select(root);
        //
        // return em.createQuery(query).getResultList();

        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {

            return em.createNamedQuery(filter.getEntity(), Jobtitle.class)
                    .setParameter(filter.getFieldName(), filter.getValues()).getResultList();

        } else {
            // FIXME itt lehet tenyleges szuroket bekotni
            return em.createNamedQuery("Jobtitle.findAll", Jobtitle.class).getResultList();
        }

    }

    @Override
    public List<StaffDocument> getDocuments(DaoFilter filter) {

        try {
            System.err.println("getDocuments " + filter.getInfo());
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StaffDocument> cq = cb.createQuery(StaffDocument.class);
            Root<StaffDocument> r = cq.from(StaffDocument.class);

            Path<String> p = r.get("documentType");
            Path<String> p2 = r.get("documentSubtype");
            cq.orderBy(cb.asc(p), cb.asc(p2));

            List<Predicate> predicateList = filter.createPredicates(cb, cq, r);
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);

            if (predicates.length == 1) {
                cq.where(predicates[0]);
            } else if (predicates.length > 1) {
                cq.where(filter.isAnyEnought() ? cb.or(predicates) : cb.and(predicates));
            }

            TypedQuery<StaffDocument> tq = em.createQuery(cq);

            return tq.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Workgroup> getWorkgroups(DaoFilter filter) {
        // CriteriaBuilder builder = em.getCriteriaBuilder();
        // CriteriaQuery<Workgroup> query = builder.createQuery(Workgroup.class);
        // Root<Workgroup> root = query.from(Workgroup.class);
        // query.select(root);
        //
        // return em.createQuery(query).getResultList();

        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {

            return em.createNamedQuery(filter.getEntity(), Workgroup.class)
                    .setParameter(filter.getFieldName(), filter.getValues()).getResultList();

        } else {
            // FIXME itt lehet tenyleges szuroket bekotni
            return em.createNamedQuery("Workgroup.findAll", Workgroup.class).getResultList();
        }

    }

    @Override
    public List<Workplace> getWorkplaces(DaoFilter filter) {

        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {

            return em.createNamedQuery(filter.getEntity(), Workplace.class)
                    .setParameter(filter.getFieldName(), filter.getValues()).getResultList();

        } else {
            // FIXME itt lehet tenyleges szuroket bekotni
            return em.createNamedQuery("Workplace.findAll", Workplace.class).getResultList();
        }

    }

    /*
     * 
     * EntityManager em = emf.createEntityManager();
     * CriteriaBuilder cb = em.getCriteriaBuilder();
     * CriteriaQuery<BaseDataType> cq = cb.createQuery(BaseDataType.class);
     * Root<BaseDataType> r = cq.from(BaseDataType.class);
     * Subquery<BaseData> subQuery = cq.subquery(BaseData.class);
     * Root<BaseData> subQueryRoot = subQuery.from(BaseData.class);
     * Predicate p1 = cb.like(subQueryRoot.<String>get("value"), "%fix%");
     * Predicate p2 = cb.equal(r.get("baseData"), subQueryRoot);
     * subQuery.where(cb.and(p1, p2));
     * cq.where((cb.exists(subQuery)));
     * TypedQuery<BaseDataType> tq = em.createQuery(cq);
     * List<BaseDataType> results = tq.getResultList();
     * for (BaseDataType result : results) {
     * System.out.println(result.getId() + " " + result.getName());
     * }
     */

    // public void create(DocumentQueue data) {
    //
    // Validate.notNull(data);
    //
    // em.persist(data);
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public void update(DocumentQueue data) {
    //
    // Validate.notNull(data);
    //
    // em.merge(data);
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Transactional
    // public void delete(Long id) {
    //
    // Validate.notNull(id);
    //
    // DocumentQueue data = find(id);
    // em.remove(data);
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public DocumentQueue find(Long id) {
    //
    // Validate.notNull(id);
    //
    // return em.find(DocumentQueue.class, id);
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public List<DocumentQueue> list(Collection<ProcessStatus> statuses) {
    //
    // Validate.notNull(statuses);
    //
    // String q =
    // "select d from DocumentQueue d where d.status in (:status) order by d.created desc";
    //
    // TypedQuery<DocumentQueue> query = em.createQuery(q, DocumentQueue.class);
    // query.setParameter("status", statuses);
    // return query.getResultList();
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public List<DocumentQueue> search(String searchText,
    // Collection<ProcessStatus> statuses) {
    //
    // Validate.notNull(statuses);
    //
    // TypedQuery<DocumentQueue> query =
    // em.createQuery("select d from DocumentQueue d " +
    // "where d.status in (:status) "
    // + "and (d.userName like :searchText " +
    // "or d.resourceType like :searchText " +
    // "or d.documentName like :searchText) "
    // + "order by d.created desc", DocumentQueue.class);
    // query.setParameter("status", statuses);
    // query.setParameter("searchText", "%" + searchText + "%");
    // return query.getResultList();
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public List<DocumentQueue> processList(Collection<ProcessStatus>
    // statuses, int limit) {
    //
    // Validate.notNull(statuses);
    //
    // String q =
    // "select d from DocumentQueue d JOIN FETCH d.documentResources where d.status in (:status)";
    //
    // TypedQuery<DocumentQueue> query = em.createQuery(q, DocumentQueue.class);
    // query.setParameter("status", statuses);
    // query.setMaxResults(limit);
    // return query.getResultList();
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public int deleteDocumentsFrom(Date date) {
    //
    // Validate.notNull(date);
    //
    // Query q =
    // em.createQuery("DELETE FROM DocumentQueue d WHERE d.created <= :date");
    // q.setParameter("date", date);
    //
    // return q.executeUpdate();
    // }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
