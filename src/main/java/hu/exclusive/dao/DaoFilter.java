package hu.exclusive.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import hu.exclusive.utils.ObjectUtils;

public class DaoFilter {

	public static final Date MINDATE = new Date(-5364666000000L); // '1800.01.01'
																	// Fontos,
																	// mert a
																	// Long.MIN-re
																	// nem talál
																	// semmit,
																	// az
																	// egy
																	// értelmetlen
																	// nagy
																	// datum,
																	// hiaba MIN
	public static final Date MAXDATE = new Date(32503676400000L); // '3000.01.01'
																	// Fontos,
																	// hogy a
																	// Long.MAX-ra
																	// nem
																	// müködik

	public enum RELATION {
		ISNULL, NOTNULL, IN, LIKE, BETWEEN, LESS, GREATER, LESSEQ, GREATEREQ, EQUAL, NONEQUAL, NAMED_QUERY;

		String info() {
			switch (this) {
			case ISNULL:
				return "nem üres";
			case NOTNULL:
				return "üres";
			case IN:
				return "a kiválasztottakból valamelyik";
			case LIKE:
				return "tartalmazza a szöveget";
			case BETWEEN:
				return "a határértékek között van";
			case LESS:
				return "kevesebb mint";
			case LESSEQ:
				return "kevesebb vagy egyenlő mint";
			case GREATER:
				return "több mint";
			case GREATEREQ:
				return "több vagy egyenlő mint";
			case EQUAL:
				return "egyenlő";
			case NONEQUAL:
				return "nem egyenlő";

			default:
				return this.name();
			}

		};

		boolean printable() {
			switch (this) {
			case ISNULL:
			case NOTNULL:
			case BETWEEN:
			case IN:
			case EQUAL:
			case NONEQUAL:
				return false;
			case LIKE:
			case LESS:
			case LESSEQ:
			case GREATER:
			case GREATEREQ:
				return true;
			default:
				return false;
			}
		};
	}

	private long totalCount;
	private int startIndex;
	private int pageSize;

	private boolean anyEnought = false;

	public DaoFilter(String emtityOrNamedQuery, String fieldName, RELATION relation, Object anyValue) {
		this._fieldName = fieldName;
		this._relation = relation;
		this._values = anyValue;
		this._entity = emtityOrNamedQuery;
	}

	public DaoFilter() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[_entity=" + _entity + ", _fieldName=" + _fieldName + ", _relation="
				+ _relation + ", _values=" + ObjectUtils.toString(_values)
				+ (filterItems.isEmpty() ? "" : ", items: " + ObjectUtils.toString(filterItems)) + "]";
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSite) {
		this.pageSize = pageSite;
	}

	private String _fieldName, _entity;
	private Object _values;
	private RELATION _relation;
	private IExitsPredicator _exists;
	public List<DaoFilter> filterItems = new ArrayList<DaoFilter>();

	/**
	 * Realation should be in: 'in', 'like', '==', '!=', '>=', '<=', '>', '<',
	 * 'notnull', 'isnull', 'between' <br>
	 * Values should be: String, Integer, Double, java.util.Date, Collection<T>,
	 * null
	 * 
	 * @param fieldName
	 * @param relation
	 * @param values
	 */
	public void addFilter(String fieldName, RELATION relation, Object anyValue) {
		this.addFilter((String) null, fieldName, relation, anyValue);
	}

	public void addFilter(IExitsPredicator exists, String fieldName, RELATION relation, Object anyValue) {
		DaoFilter _item = new DaoFilter(null, fieldName, relation, anyValue);
		_item._exists = exists;

		if (!filterItems.contains(_item)) {
			filterItems.add(_item);
		}
	}

	public String getEntity() {
		return _entity;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public Object getValues() {
		return _values;
	}

	@SuppressWarnings("unchecked")
	public Object getValue(String parameterName) {
		return (_values != null && (_values instanceof Map)) ? ((Map<String, Object>) _values).get(parameterName)
				: null;
	}

	public RELATION getRelation() {
		return _relation;
	}

	/**
	 * Relation should be in: 'in', 'like', '==', '!=', '>=', '<=', '>', '<',
	 * 'notnull', 'isnull', 'between' subEntity should be related Entity
	 * property entity or entities class.
	 * 
	 * @param subEntity
	 * @param fieldName
	 * @param relation
	 * @param anyValue
	 */
	public void addFilter(String subEntity, String fieldName, RELATION relation, Object anyValue) {
		DaoFilter _item = new DaoFilter(subEntity, fieldName, relation, anyValue);

		if (relation != RELATION.ISNULL && relation != RELATION.NOTNULL && ObjectUtils.nv(anyValue) == null)
			return;

		if (!filterItems.contains(_item)) {
			filterItems.add(_item);
		}

	}

	/**
	 * Leveszi a filterekből a filter (amit előtte a getFilter() ad vissza).
	 * 
	 * @param filter
	 */
	public void removeFilter(DaoFilter filter) {
		if (filter != null) {
			System.out.println("remove filter " + filter);
			filterItems.remove(filter);
		}
	}

	/**
	 * Visszaadja az elso filtert amire igazak a paraméterek. Null values esetén
	 * azt nem veszi figyelembe, csak a field adataival megtalált első filtert
	 * adja vissza. Ahol egy filter több értékkel is szerepelhet, ott addig kell
	 * hívni, amíg nem null a találat.
	 * 
	 * @param fieldName
	 * @param relation
	 * @param values
	 * @return
	 */
	public DaoFilter getFilter(String fieldName, RELATION relation, Object anyValue) {

		DaoFilter f = null;
		for (DaoFilter daoFilter : filterItems) {
			if (daoFilter._fieldName.equals(fieldName) && daoFilter._relation == (relation)) {
				if (anyValue != null && anyValue.equals(daoFilter._values)) {
					// megvan pontosan a filter
					return daoFilter;
				} else if (ObjectUtils.nv(anyValue) == null) {
					// az elso megvan, lehet meg tobb is
					return daoFilter;
				}
				// lehet ez
				f = daoFilter;
			}
		}
		return f;
	}

	public <T> List<Predicate> createPredicates(CriteriaBuilder builder, CriteriaQuery<?> query, Root<T> root) {
		List<Predicate> predicateList = new ArrayList<Predicate>();

		System.err.println(
				"predicate anyEnought=" + anyEnought + " filteritems: " + filterItems == null ? this : filterItems);

		if (filterItems.isEmpty() && this.getFieldName() != null) {
			predicateFilter(builder, query, root, predicateList, this);
		}
		for (DaoFilter item : filterItems) {

			predicateFilter(builder, query, root, predicateList, item);
		}

		return predicateList;
	}

	private <T> void predicateFilter(CriteriaBuilder builder, CriteriaQuery<?> query, Root<T> root,
			List<Predicate> predicateList, DaoFilter item) {
		Path<T> field = null;
		Predicate p = null;
		if (item._exists != null) {
			// exist joiner tablaban levo mezo
			item._exists.preperaPredicate(predicateList, item, builder, query, root);

		} else {
			if (item._entity != null) {
				// csatolt entity-ben lévő property
				Join<T, ?> joinedEntity = root.join(item._entity, JoinType.LEFT);
				field = joinedEntity.get(item._fieldName);
				p = createPredicate(builder, field, item._relation, item._values);

			} else {
				// sima property
				field = root.get(item._fieldName);
				p = createPredicate(builder, field, item._relation, item._values);
			}

			if (p != null)
				predicateList.add(p);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Predicate createPredicate(CriteriaBuilder builder, Path<T> field, RELATION relation, Object value) {

		if (relation == RELATION.IN) {

			if (ObjectUtils.isArray(value) || ObjectUtils.isCollection(value)) {

				Collection<?> v = ObjectUtils.toCollection(value, Object.class);
				if (v.size() > 0) {
					return field.in(v);
				}
			}

		} else if (RELATION.LIKE == (relation)) {

			String pattern = "%" + ((String) value).toUpperCase() + "%";
			return builder.like(builder.upper((Path<String>) field), pattern);

		} else if (RELATION.EQUAL == (relation)) {

			return builder.equal(field, value);

		} else if (RELATION.NONEQUAL == (relation)) {

			return builder.notEqual(field, value);

		} else if (RELATION.GREATEREQ == (relation) && value != null) {

			if (value instanceof String) {

				return builder.greaterThanOrEqualTo((Path<String>) field, (String) value);

			} else if (value instanceof Integer) {

				return builder.greaterThanOrEqualTo((Path<Integer>) field, (Integer) value);

			} else if (value instanceof Double) {

				return builder.greaterThanOrEqualTo((Path<Double>) field, (Double) value);

			} else if (value instanceof Date) {

				return builder.greaterThanOrEqualTo((Path<Date>) field, (Date) value);

			}

		} else if (RELATION.LESSEQ == (relation) && value != null) {

			if (value instanceof String) {

				return builder.lessThanOrEqualTo((Path<String>) field, (String) value);

			} else if (value instanceof Integer) {

				return builder.lessThanOrEqualTo((Path<Integer>) field, (Integer) value);

			} else if (value instanceof Double) {

				return builder.lessThanOrEqualTo((Path<Double>) field, (Double) value);

			} else if (value instanceof Date) {

				return builder.lessThanOrEqualTo((Path<Date>) field, (Date) value);

			}

		} else if (RELATION.GREATER == (relation) && value != null) {

			if (value instanceof String) {

				return builder.greaterThan((Path<String>) field, (String) value);

			} else if (value instanceof Integer) {

				return builder.greaterThan((Path<Integer>) field, (Integer) value);

			} else if (value instanceof Double) {

				return builder.greaterThan((Path<Double>) field, (Double) value);

			} else if (value instanceof Date) {

				return builder.greaterThan((Path<Date>) field, (Date) value);

			}

		} else if (RELATION.LESS == (relation) && value != null) {

			if (value instanceof String) {

				return builder.lessThan((Path<String>) field, (String) value);

			} else if (value instanceof Integer) {

				return builder.lessThan((Path<Integer>) field, (Integer) value);

			} else if (value instanceof Double) {

				return builder.lessThan((Path<Double>) field, (Double) value);

			} else if (value instanceof Date) {

				return builder.lessThan((Path<Date>) field, (Date) value);

			}

		} else if (RELATION.BETWEEN == (relation)) {
			// ez egyelore datumra muxik csak
			if (ObjectUtils.isArray(value)) {
				try {
					Date[] dates = (Date[]) (value);
					if (dates.length == 2) {
						return builder.between((Path<Date>) field, dates[0], dates[1]);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (RELATION.NOTNULL == (relation)) {

			return builder.isNotNull(field);

		} else if (RELATION.ISNULL == (relation)) {

			return builder.isNull(field);

		}

		return null;

	}

	public boolean isAnyEnought() {
		return anyEnought;
	}

	public boolean isOr() {
		return isAnyEnought();
	}

	public void setAnyEnought(boolean anyEnought) {
		this.anyEnought = anyEnought;
	}

	public void reset() {
		System.out.println("DaoFilter reset");
		filterItems.clear();
	}

	public String getInfo() {
		if (_relation != null) {
			return "{" + _fieldName + "} " + _relation.info()
					+ (_relation.printable() ? (" <b>'" + ObjectUtils.toString(_values) + "'</b>") : "");
		}
		return "";
	}

	@Override
	public boolean equals(Object obj) {
		DaoFilter df = (DaoFilter) obj;
		return ("" + this._fieldName + this._relation + ObjectUtils.nv(this._values))
				.equals("" + df._fieldName + df._relation + ObjectUtils.nv(df._values));
	}

	public void setValues(Object anyValue) {
		this._values = anyValue;
	}

	public void setFieldName(String fieldName) {
		this._fieldName = fieldName;
	}

	public void setEntity(String entity) {
		this._entity = entity;
	}

	public void setRelation(RELATION relation) {
		this._relation = relation;
	}

	public List<DaoFilter> getFilterItems() {
		return filterItems;
	}
}
