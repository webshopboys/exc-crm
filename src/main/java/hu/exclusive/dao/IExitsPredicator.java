package hu.exclusive.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A filter szuréséhez a kapcsolt táblákra exists subselect rész képző osztályok felülete.
 * 
 * @author Petnehazi
 *
 */
public interface IExitsPredicator {

    void preperaPredicate(List<Predicate> filters, DaoFilter item, CriteriaBuilder builder, CriteriaQuery<?> query,
            Root<?> baseRoot);
}
