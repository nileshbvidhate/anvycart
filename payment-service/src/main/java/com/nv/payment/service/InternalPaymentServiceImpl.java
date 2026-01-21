package com.nv.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nv.payment.dto.request.RazorpayOrderCreateRequest;
import com.nv.payment.dto.request.RazorpayPaymentVerifyRequest;
import com.nv.payment.dto.response.RazorpayOrderCreateResponse;
import com.nv.payment.dto.response.RazorpayPaymentVerifyResponse;
import com.nv.payment.entity.Payment;
import com.nv.payment.entity.PaymentStatus;
import com.nv.payment.exception.ResourceNotFoundException;
import com.nv.payment.mapper.PaymentMapper;
import com.nv.payment.repository.PaymentRepository;
import com.nv.payment.util.RazorpaySignatureUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class InternalPaymentServiceImpl implements InternalPaymentService {

	private final PaymentRepository paymentRepository;
	private final RazorpayClient razorpayClient;
	private final PaymentMapper paymentMapper;
	private final String razorpayKeySecret;

	public InternalPaymentServiceImpl(PaymentRepository paymentRepository, RazorpayClient razorpayClient,
			PaymentMapper paymentMapper, @Value("${razorpay.key-secret}") String razorpayKeySecret) {
		this.paymentRepository = paymentRepository;
		this.razorpayClient = razorpayClient;
		this.paymentMapper = paymentMapper;
		this.razorpayKeySecret = razorpayKeySecret;
	}

	@Override
	public RazorpayOrderCreateResponse createOrder(RazorpayOrderCreateRequest request) {

		// 1. Normalize currency
		String currency = request.getCurrency().toUpperCase();

		// 2. Convert amount to paise
		long amountInPaise = request.getAmount().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP)
				.longValueExact();

		// 3. Prepare Razorpay order request
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amountInPaise);
		orderRequest.put("currency", currency);
		orderRequest.put("receipt", "orderId_" + request.getOrderId());

		try {
			// 4. Create Razorpay order
			Order razorpayOrder = razorpayClient.orders.create(orderRequest);

			// 5. Persist payment in CREATED state
			Payment payment = Payment.builder().orderId(request.getOrderId()).amount(request.getAmount())
					.currency(currency).status(PaymentStatus.CREATED).razorpayOrderId(razorpayOrder.get("id")).build();

			paymentRepository.save(payment);

			// 6. Return mapped response
			return paymentMapper.toRazorpayOrderCreateResponse(payment);

		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to create Razorpay order", e);
		}
	}

	@Override
	public RazorpayPaymentVerifyResponse verifyPayment(RazorpayPaymentVerifyRequest request) {

		Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		// Prevent re-verification
		if (payment.getStatus() == PaymentStatus.CAPTURED) {
			return new RazorpayPaymentVerifyResponse(PaymentStatus.CAPTURED.name());
		}

		String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();

		boolean valid = RazorpaySignatureUtil.verify(payload, razorpayKeySecret, request.getRazorpaySignature());

		if (!valid) {
			
			payment.setStatus(PaymentStatus.FAILED);
			
			paymentRepository.save(payment);

			return new RazorpayPaymentVerifyResponse(PaymentStatus.FAILED.name());
		}

		// Valid signature
		payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
		payment.setRazorpaySignature(request.getRazorpaySignature());
		payment.setStatus(PaymentStatus.AUTHORIZED);

		paymentRepository.save(payment);

		return new RazorpayPaymentVerifyResponse(PaymentStatus.AUTHORIZED.name());
	}
}
