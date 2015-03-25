package hu.exclusive.dao;

import hu.exclusive.dao.model.Alarm;
import hu.exclusive.dao.model.StaffAlarmK;

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
public class StaffExistsJobtitle implements IExitsPredicator {

    @SuppressWarnings("unchecked")
    @Override
    public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<?> baseRoot) {

        // javax.persistence.criteria.CriteriaQuery<Staff> staffQuery = (javax.persistence.criteria.CriteriaQuery<Staff>) query;
        // Root<Staff> staff = (Root<Staff>) baseRoot;

        Subquery<Alarm> alarmJoin = query.subquery(Alarm.class);
        Root<Alarm> alarm = alarmJoin.from(Alarm.class);
        Root<StaffAlarmK> alarmKey = alarmJoin.from(StaffAlarmK.class);
        alarmJoin.select(alarm);

        Predicate p1 = builder.equal(alarm.get("idAlarm"), alarmKey.get("idAlarm"));
        Predicate p2 = builder.equal(baseRoot.get("idStaff"), alarmKey.get("idStaff"));

        {
            // Itt lehetne több kereső mezőt is kezelni és akkor több px jönne még hozzá (pl: több mezőben like)
            final Path<?> field = alarm.get(item.getFieldName());

            final DaoFilter.RELATION relation = item.getRelation();

            Object value = item.getValues();

            Predicate p3 = item.createPredicate(builder, field, relation, value);

            alarmJoin.where(builder.and(p1, p2, p3));
        }

        filters.add(builder.exists(alarmJoin));
    }
}
