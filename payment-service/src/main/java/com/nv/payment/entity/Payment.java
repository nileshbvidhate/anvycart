package com.nv.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;
	
	@Column(nullable = false)
	private String currency;

	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "razorpay_order_id", nullable = false)
	private String razorpayOrderId;

	@Column(name = "razorpay_payment_id")
	private String razorpayPaymentId;

	@Column(name = "razorpay_signature")
	private String razorpaySignature;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
