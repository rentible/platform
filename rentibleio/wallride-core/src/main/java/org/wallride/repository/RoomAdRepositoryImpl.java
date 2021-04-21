package org.wallride.repository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Property;
import org.wallride.domain.Property_;
import org.wallride.domain.RoomAd;
import org.wallride.domain.RoomAd_;
import org.wallride.model.PropertyRequest;
import org.wallride.model.PropertySearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoomAdRepositoryImpl implements RoomAdRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @param propertyRequest
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Property> search(PropertyRequest propertyRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Property> cq = cb.createQuery(Property.class);
		Root<Property> rootEntry = cq.from(Property.class);
		CriteriaQuery<Property> all = cq.select(rootEntry);
		TypedQuery<Property> allQuery = entityManager.createQuery(all);

		List<Property> results = allQuery.getResultList();
		int resultSize = results.size();

		return new PageImpl<>(results, pageable, resultSize);
	}

	@Override
	public List<RoomAd> findAllByCustomFilter(PropertySearchRequest propertySearchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoomAd> cq = cb.createQuery(RoomAd.class);
		List<Predicate> predicates = new ArrayList<>();
		Date utilDate = new Date();
		Calendar cal = Calendar.getInstance();
		List<Order> orderList = new ArrayList<>();

		Root<RoomAd> ad = cq.from(RoomAd.class);

		if (CollectionUtils.isNotEmpty(propertySearchRequest.getRentPeriods())) {

			Join<RoomAd, Property> property = ad.join(RoomAd_.property);

			Predicate termOfLeasePredicate = property.join(Property_.termOfLease).in(propertySearchRequest.getRentPeriods());
			predicates.add(termOfLeasePredicate);
		}

		if (propertySearchRequest.getFrom() != null) {
			cal.setTime(propertySearchRequest.getFrom());
			cal.set(Calendar.MILLISECOND, 0);
			Predicate availableFromPredicate = cb.greaterThan(ad.get("property").get("availableFrom"), new java.sql.Timestamp(utilDate.getTime()));
			predicates.add(availableFromPredicate);
		}

		if (propertySearchRequest.getCurrency() != null && propertySearchRequest.getCurrency() != 0) {
			Predicate currencyPredicate = cb.equal(ad.get("property").get("rentPriceCurrency"), propertySearchRequest.getCurrency());
			predicates.add(currencyPredicate);
		}

		if (propertySearchRequest.getMinPrice() != null) {
			Predicate minPricePredicate = cb.greaterThan(ad.get("property").get("rentPrice"), propertySearchRequest.getMinPrice());
			predicates.add(minPricePredicate);
		}

		if (propertySearchRequest.getMaxPrice() != null) {
			Predicate maxPricePredicate = cb.lessThan(ad.get("property").get("rentPrice"), propertySearchRequest.getMaxPrice());
			predicates.add(maxPricePredicate);
		}

		if (propertySearchRequest.getAuthUserId() != null) {
			Predicate maxPricePredicate = cb.equal(ad.get("createdBy"), propertySearchRequest.getAuthUserId());
			predicates.add(maxPricePredicate);
		}

		Predicate enabledPredicate = cb.equal(ad.get("enabled"), true);
		predicates.add(enabledPredicate);

		if (!predicates.isEmpty()) {
			cq.where(predicates.toArray(new Predicate[] {}));
		}

		orderList.add(cb.desc(ad.get("createdOn")));

		cq.orderBy(orderList);

		TypedQuery<RoomAd> query = entityManager.createQuery(cq);

		return query.getResultList();
	}

}
