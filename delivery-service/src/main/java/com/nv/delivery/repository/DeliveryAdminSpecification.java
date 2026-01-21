package com.nv.delivery.repository;

import com.nv.delivery.entity.Delivery;
import com.nv.delivery.entity.DeliveryStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAdminSpecification {

	private DeliveryAdminSpecification() {
		// utility class
	}

	public static Specification<Delivery> build(DeliveryStatus status, Long orderId, Long userId,
			LocalDateTime fromDate, LocalDateTime toDate) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (status != null) {
				predicates.add(cb.equal(root.get("deliveryStatus"), status));
			}

			if (orderId != null) {
				predicates.add(cb.equal(root.get("orderId"), orderId));
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
