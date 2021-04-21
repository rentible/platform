package org.wallride.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Property;
import org.wallride.model.PropertySearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @param propertySearchRequest
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Property> search(PropertySearchRequest propertySearchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Property> cq = cb.createQuery(Property.class);
		Root<Property> rootEntry = cq.from(Property.class);
		CriteriaQuery<Property> all = cq.select(rootEntry);
		TypedQuery<Property> allQuery = entityManager.createQuery(all);

		List<Property> results = allQuery.getResultList();
		int resultSize = results.size();

		return new PageImpl<>(results, pageable, resultSize);
	}

}
