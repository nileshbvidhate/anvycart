package com.nv.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.nv.order.entity.Order;
import com.nv.order.entity.OrderStatus;

import jakarta.persistence.criteria.Predicate;

public class SpecificationBuilder {

	public static Specification<Order> withFilters(OrderStatus status, Long userId, LocalDateTime fromDate,
			LocalDateTime toDate) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (status != null) {
				predicates.add(cb.equal(root.get("status"), status));
			}

			if (userId != null) {
				predicates.add(cb.equal(root.get("userId"), userId));
			}

			if (fromDate != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
			}

			if (toDate != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
