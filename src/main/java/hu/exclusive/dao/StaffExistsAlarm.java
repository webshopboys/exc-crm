package hu.exclusive.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import hu.exclusive.dao.model.Alarm;
import hu.exclusive.dao.model.StaffAlarmK;

/**
 * A STAFF rekordok lekérdezése a kapcsolódó ALARM alapján.
 * 
 * @author Petnehazi
 *
 */
public class StaffExistsAlarm implements IExitsPredicator {

	@Override
	public void preperaPredicate(java.util.List<Predicate> filters, DaoFilter item, CriteriaBuilder builder,
			CriteriaQuery<?> query, Root<?> baseRoot) {

		/*
		 * A baseRoot STAFF. Ehhez egy join az ALARM a K_STAFF_ALARM tablan at.
		 * Ezzel osszekotjuk, a megfelelo subselect-tel peremeterezuk es
		 * exist-sel bekotjuk.
		 */
		Subquery<Alarm> alarmJoin = query.subquery(Alarm.class);
		Root<Alarm> alarm = alarmJoin.from(Alarm.class);
		Root<StaffAlarmK> alarmKey = alarmJoin.from(StaffAlarmK.class);
		alarmJoin.select(alarm);

		Predicate join1 = builder.equal(alarm.get("idAlarm"), alarmKey.get("idAlarm"));
		Predicate join2 = builder.equal(baseRoot.get("idStaff"), alarmKey.get("idStaff"));

		{
			// Itt lehetne több kereső mezőt is kezelni és akkor több px jönne
			// még hozzá (pl: több mezőben like)
			final Path<?> field = alarm.get(item.getFieldName());

			final DaoFilter.RELATION relation = item.getRelation();

			Object value = item.getValues();

			Predicate search1 = item.createPredicate(builder, field, relation, value);

			alarmJoin.where(builder.and(join1, join2, search1));
		}

		filters.add(builder.exists(alarmJoin));
	}
}
