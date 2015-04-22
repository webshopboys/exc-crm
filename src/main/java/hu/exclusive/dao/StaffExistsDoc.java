package hu.exclusive.dao;

import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.StaffNote;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó DOCUMENTS alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsDoc implements IExitsPredicator {

    @SuppressWarnings("unchecked")
    @Override
    public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {
        predicateContract(filters, item, builder, query, baseRoot);
        predicateDoctor(filters, item, builder, query, baseRoot);
        predicateNote(filters, item, builder, query, baseRoot);
        predicateAttachment(filters, item, builder, query, baseRoot);
    }

    private void predicateContract(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {
        //
        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<ContractDoc> existsDoc = query.subquery(ContractDoc.class);
        Root<ContractDoc> doc = existsDoc.from(ContractDoc.class);
        existsDoc.select(doc);

        Predicate join1 = builder.equal(baseRoot.get("idStaff"), doc.get("idStaff"));

        // filters.add(cb.exists(existsDoc));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = doc.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();

            Predicate search = item.createPredicate(builder, field, relation, value);

            // Predicate p1 = builder.like(doc.<String> get("documentNote"), "%fix%");

            existsDoc.where(builder.and(join1, search));
        }

        filters.add(builder.exists(existsDoc));
    }

    private void predicateAttachment(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {
        //
        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<Attachment> existsDoc = query.subquery(Attachment.class);
        Root<Attachment> doc = existsDoc.from(Attachment.class);
        existsDoc.select(doc);

        Predicate join1 = builder.equal(baseRoot.get("idStaff"), doc.get("idStaff"));

        // filters.add(cb.exists(existsDoc));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = doc.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();

            Predicate search = item.createPredicate(builder, field, relation, value);

            // Predicate p1 = builder.like(doc.<String> get("documentNote"), "%fix%");

            existsDoc.where(builder.and(join1, search));
        }

        filters.add(builder.exists(existsDoc));
    }

    private void predicateDoctor(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {
        //
        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<DrDoc> existsDoc = query.subquery(DrDoc.class);
        Root<DrDoc> doc = existsDoc.from(DrDoc.class);
        existsDoc.select(doc);

        Predicate join1 = builder.equal(baseRoot.get("idStaff"), doc.get("idStaff"));

        // filters.add(cb.exists(existsDoc));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = doc.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();

            Predicate search = item.createPredicate(builder, field, relation, value);

            // Predicate p1 = builder.like(doc.<String> get("documentNote"), "%fix%");

            existsDoc.where(builder.and(join1, search));
        }

        filters.add(builder.exists(existsDoc));
    }

    private void predicateNote(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {
        //
        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<StaffNote> existsDoc = query.subquery(StaffNote.class);
        Root<StaffNote> doc = existsDoc.from(StaffNote.class);
        existsDoc.select(doc);

        Predicate join1 = builder.equal(baseRoot.get("idStaff"), doc.get("idStaff"));

        // filters.add(cb.exists(existsDoc));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = doc.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();

            Predicate search = item.createPredicate(builder, field, relation, value);

            // Predicate p1 = builder.like(doc.<String> get("documentNote"), "%fix%");

            existsDoc.where(builder.and(join1, search));
        }

        filters.add(builder.exists(existsDoc));
    }
}
