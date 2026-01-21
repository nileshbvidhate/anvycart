package com.nv.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nv.payment.entity.Payment;
import com.nv.payment.entity.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    
    List<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId);
    
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
}
