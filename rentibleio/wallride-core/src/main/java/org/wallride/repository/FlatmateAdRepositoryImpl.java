package org.wallride.repository;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.FlatmateAd;
import org.wallride.model.FlatmateSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FlatmateAdRepositoryImpl implements FlatmateAdRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @param flatmateSearchRequest
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<FlatmateAd> search(FlatmateSearchRequest flatmateSearchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<FlatmateAd> cq = cb.createQuery(FlatmateAd.class);
		Root<FlatmateAd> rootEntry = cq.from(FlatmateAd.class);
		CriteriaQuery<FlatmateAd> all = cq.select(rootEntry);
		TypedQuery<FlatmateAd> allQuery = entityManager.createQuery(all);

		List<FlatmateAd> results = allQuery.getResultList();
		int resultSize = results.size();

		return new PageImpl<>(results, pageable, resultSize);
	}

	@Override
	public List<FlatmateAd> findAllByCustomFilter(FlatmateSearchRequest flatmateSearchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<FlatmateAd> cq = cb.createQuery(FlatmateAd.class);
		List<Predicate> predicates = new ArrayList<>();
		List<Order> orderList = new ArrayList<>();
		Calendar cal;

		Root<FlatmateAd> ad = cq.from(FlatmateAd.class);

		Predicate enabledPredicate = cb.equal(ad.get("enabled"), true);
		predicates.add(enabledPredicate);

		if (flatmateSearchRequest.getArea() != null && StringUtils.isNotBlank(flatmateSearchRequest.getArea())) {
			Predicate areaPredicate = cb.like(ad.get("area"), "%" + flatmateSearchRequest.getArea() + "%");
			predicates.add(areaPredicate);
		}

		if (CollectionUtils.isNotEmpty(flatmateSearchRequest.getRentPeriods())) {
			Predicate termOfLeasePredicate = ad.join("termOfLease").in(flatmateSearchRequest.getRentPeriods());
			predicates.add(termOfLeasePredicate);
		}

		if (flatmateSearchRequest.getGender() != null && flatmateSearchRequest.getGender() != 0) {
			Predicate genderPredicate = cb.equal(ad.get("user").get("userDetail").get("gender"), flatmateSearchRequest.getGender());
			predicates.add(genderPredicate);
		}

		if (flatmateSearchRequest.getName() != null && StringUtils.isNotBlank(flatmateSearchRequest.getName())) {
			Expression<String> fullName = cb.concat(ad.get("user").get("userDetail").get("firstName"), ad.get("user").get("userDetail").get("lastName"));
			flatmateSearchRequest.setName(flatmateSearchRequest.getName().replaceAll("\\s+", ""));
			Predicate namePredicate = cb.like(cb.upper(fullName), "%" + flatmateSearchRequest.getName().toUpperCase() + "%");
			predicates.add(namePredicate);
		}

		if (flatmateSearchRequest.getMinAge() != null) {
			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -flatmateSearchRequest.getMinAge());

			Date in = cal.getTime();
			LocalDateTime minAge = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());

			Predicate minAgePredicate = cb.lessThanOrEqualTo(ad.get("user").get("userDetail").get("dateOfBirth"), minAge);
			predicates.add(minAgePredicate);
		}

		if (flatmateSearchRequest.getMaxAge() != null) {
			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -flatmateSearchRequest.getMaxAge());

			Date in = cal.getTime();
			LocalDateTime maxAge = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());

			Predicate minAgePredicate = cb.greaterThanOrEqualTo(ad.get("user").get("userDetail").get("dateOfBirth"), maxAge);
			predicates.add(minAgePredicate);
		}

		if (flatmateSearchRequest.getAuthUserId() != null) {
			Predicate maxPricePredicate = cb.equal(ad.get("createdBy"), flatmateSearchRequest.getAuthUserId());
			predicates.add(maxPricePredicate);
		}

		if (!predicates.isEmpty()) {
			cq.where(predicates.toArray(new Predicate[] {}));
		}

		orderList.add(cb.desc(ad.get("createdOn")));

		cq.orderBy(orderList);

		TypedQuery<FlatmateAd> query = entityManager.createQuery(cq);

		return query.getResultList();
	}
}
