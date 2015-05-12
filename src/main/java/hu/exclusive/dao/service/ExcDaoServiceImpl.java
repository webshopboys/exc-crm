package hu.exclusive.dao.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.EntityCommons;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Role;
import hu.exclusive.dao.model.SSystem;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.dao.model.StaffNote;
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

import org.hibernate.metamodel.domain.Entity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ExcDaoServiceImpl implements IExcDaoService {

    @PersistenceContext
    private EntityManager em;

    public ExcDaoServiceImpl() {
        System.err.println("ExcDaoServiceImpl implements IExcDaoService created. " + hashCode());
    }

    @Transactional
    @Override
    public void saveStaff(StaffDetail staff) {
        System.err.println("ExcDaoServiceImpl.saveStaff(" + staff + ")");
        if (staff.getIdStaff() == null)
            em.persist(staff);
        else
            em.merge(staff);
    }

    @Transactional
    @Override
    public void saveDocument(Attachment document) {
        if (document.getId() == null)
            em.persist(document);
        else
            em.merge(document);
    }

    @Transactional
    @Override
    public void saveDocument(ContractDoc document) {
        try {
            if (document.getId() == null)
                em.persist(document);
            else
                em.merge(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional
    @Override
    public void saveDocument(DrDoc document) {
        if (document.getId() == null)
            em.persist(document);
        else
            em.merge(document);
    }

    @Transactional
    @Override
    public void saveDocument(StaffNote document) {
        if (document.getId() == null)
            em.persist(document);
        else
            em.merge(document);
    }

    @Transactional
    @Override
    public List<ContractDoc> getContractDocs(DaoFilter filter) {

        if (filter == null) {
            return em.createNamedQuery("ContractDoc.findAll", ContractDoc.class).getResultList();

        } else if (DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {

            return getNamedEntities(ContractDoc.class, filter);

        } else {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ContractDoc> cq = cb.createQuery(ContractDoc.class);

            Root<ContractDoc> r = cq.from(ContractDoc.class);

            Path<String> p = r.get("documentType");
            cq.orderBy(cb.asc(p));

            List<Predicate> predicateList = filter.createPredicates(cb, cq, r);
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);

            if (predicates.length == 1) {
                cq.where(predicates[0]);
            } else if (predicates.length > 1) {
                cq.where(filter.isAnyEnought() ? cb.or(predicates) : cb.and(predicates));
            }

            TypedQuery<ContractDoc> tq = em.createQuery(cq);
            return tq.getResultList();
        }
    }

    @Transactional
    @Override
    public List<DrDoc> getDrDocs(DaoFilter filter) {
        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {
            return getNamedEntities(DrDoc.class, filter);
        } else {
            return em.createNamedQuery("DrDoc.findAll", DrDoc.class).getResultList();
        }
    }

    @Transactional
    @Override
    public List<StaffNote> getStaffNotes(DaoFilter filter) {
        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {
            return getNamedEntities(StaffNote.class, filter);

        } else {
            return em.createNamedQuery("StaffNote.findAll", StaffNote.class).getResultList();
        }
    }

    @Transactional
    @Override
    public List<Attachment> getAttachments(DaoFilter filter) {
        if (filter != null && DaoFilter.RELATION.NAMED_QUERY == filter.getRelation()) {
            return getNamedEntities(Attachment.class, filter);
        } else {
            return em.createNamedQuery("Attachment.findAll", Attachment.class).getResultList();
        }
    }

    private <T> List<T> getNamedEntities(Class<T> klass, DaoFilter filter) {

        if (filter.getFilterItems() == null || filter.getFilterItems().isEmpty()) {
            // simple filter
            return em.createNamedQuery(filter.getEntity(), klass).setParameter(filter.getFieldName(), filter.getValues())
                    .getResultList();

        } else {
            // multiple filters
            // if (true)
            // throw new RuntimeException("Not implemented yet");

            List<DaoFilter> params = filter.getFilterItems();

            String[] parameterNames = new String[params.size()];
            Object[] parameterValues = new Object[params.size()];
            String namedQuery = null;
            int x = 0;
            for (DaoFilter item : params) {
                if (namedQuery == null)
                    namedQuery = item.getEntity();
                parameterNames[x] = item.getFieldName();
                parameterValues[x] = item.getValues();
            }

            return getNamedEntities(namedQuery, klass, parameterNames, parameterValues);
        }
    }

    private <T> List<T> getNamedEntities(String namedQuery, Class<T> klass, String[] parameterNames, Object[] parameterValues) {
        TypedQuery<T> nq = em.createNamedQuery(namedQuery, klass);
        for (int i = 0; i < parameterNames.length; i++) {
            nq.setParameter(parameterNames[i], parameterValues[i]);
        }

        return nq.getResultList();
    }

    @Transactional
    @Override
    public void saveUser(CrmUser user) {

        if (user.getIdCrmUser() == null || user.getIdCrmUser() == 0)
            em.persist(user);
        else
            em.merge(user);

        System.out.println(user + " user persisted? " + user);

    }

    @Transactional
    @Override
    public void saveFunction(Function f) {
        em.merge(f);
        System.out.println(f + " function persisted? ");
    }

    @Transactional
    @Override
    public void saveRole(Role r) {
        em.merge(r);
        System.out.println(r + " role persisted? ");
    }

    @Override
    public CrmUser getUser(String login) {
        return em.createNamedQuery("CrmUser.find", CrmUser.class).setParameter("loginName", login).getSingleResult();
    }

    @Override
    public List<CrmUser> getUsers(DaoFilter filter) {
        return em.createNamedQuery("CrmUser.findAll", CrmUser.class).getResultList();

    }

    @Override
    public StaffDetail getStaffDetail(Integer idStaff) {
        return em.createNamedQuery("StaffDetail.findStaff", StaffDetail.class).setParameter("idStaff", idStaff).getSingleResult();
    }

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

        System.err.println("[startIndex=" + filter.getStartIndex() + ", pageSize=" + filter.getPageSize() + ", count " + count
                + " or " + filter.getTotalCount() + " per " + results.size());

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
    public List<Role> getRoles(DaoFilter daoFilter) {
        return em.createNamedQuery("Role.findAll", Role.class).getResultList();
    }

    @Override
    public List<Function> getFunctions(DaoFilter daoFilter) {
        return em.createNamedQuery("Function.findAll", Function.class).getResultList();
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

    @Override
    public String getSystemParameter(String sysgroup, String syskey) {
        List<SSystem> list = em.createNamedQuery("SSystem.findGroupKey", SSystem.class).setParameter("sysgroup", sysgroup)
                .setParameter("syskey", syskey).getResultList();
        if (list != null && list.size() > 0)
            return list.get(0).getSysparam();
        return "";
    }

    @Override
    public List<SSystem> getSystemParameters(String sysgroup, String syskey) {
        List<SSystem> list = em.createNamedQuery("SSystem.findGroupKey", SSystem.class).setParameter("sysgroup", sysgroup)
                .setParameter("syskey", syskey).getResultList();
        return list;
    }

    private void saveEntity(Entity entity, Object instance) {
        if (EntityCommons.class.isInstance(instance)) {
            if (((EntityCommons) instance).getId() == null) {
                em.persist(entity);
                return;
            }
        }
        em.merge(entity);
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
