package com.hotel.payment_service.service;
import com.hotel.payment_service.Exception.PaymentNotFoundException;
import com.hotel.payment_service.Exception.PaymentProcessingException;
import com.hotel.payment_service.dto.PaymentRequestDTO;
import com.hotel.payment_service.dto.PaymentRequestForStatus;
import com.hotel.payment_service.dto.PaymentResponseDTO;

import com.hotel.payment_service.feign.RoomServiceClient;
import com.hotel.payment_service.model.Payment;
import com.hotel.payment_service.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.PaymentLink;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

        private final PaymentRepository paymentRepository;
        private final RoomServiceClient roomServiceClient;

        @Value("${razorpay.api.key}")
        private String razorpayApiKey;

        @Value("${razorpay.api.secret}")
        private String razorpayApiSecret;

        public PaymentService(PaymentRepository paymentRepository, RoomServiceClient rateServiceClient) {
            this.paymentRepository = paymentRepository;
            this.roomServiceClient = rateServiceClient;
        }



    public String createPaymentLink(PaymentRequestDTO request) throws RazorpayException {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RazorpayException("Invalid amount: must be greater than zero");
        }
        RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

        System.out.println("Email Is "+request.getCustomerEmail());
        System.out.println("Amount Is "+request.getAmount());
        JSONObject options = new JSONObject();
        options.put("amount", (int)(request.getAmount() * 100));
     //   options.put("amount", request.getAmount() * 100); // in paisa
        options.put("currency", request.getCurrency());
        options.put("description", "Hotel Booking Payment for Booking ID: " + request.getBookingId());
        options.put("reference_id", "ref_" + request.getBookingId());

        JSONObject customer = new JSONObject();
        customer.put("email", request.getCustomerEmail());
        options.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        options.put("notify", notify);

        options.put("callback_url", "http://localhost:3000/confirmation?reservationCode=" + request.getBookingId()); // customize this
        options.put("callback_method", "get");

        PaymentLink paymentLink = razorpay.paymentLink.create(options);

        // Save to database
        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setCustomerEmail(request.getCustomerEmail());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentIntentId(paymentLink.get("id"));
        payment.setStatus("CREATED");
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return paymentLink.get("short_url"); // Only return the payment link
    }


    public PaymentResponseDTO createPaymentOrder(PaymentRequestDTO request) throws RazorpayException  {

            try {
                RazorpayClient    razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

                JSONObject options = new JSONObject();
                options.put("amount", request.getAmount() * 100); // Convert to paisa
                options.put("currency", request.getCurrency());
                options.put("receipt", "txn_" + request.getBookingId());
                options.put("payment_capture", 1); // Auto capture payment

                Order order = razorpay.orders.create(options);

                // Save payment details
                Payment payment = new Payment();
                payment.setBookingId(request.getBookingId());
                payment.setCustomerEmail(request.getCustomerEmail());
                payment.setAmount(request.getAmount());
                payment.setCurrency(request.getCurrency());
                payment.setPaymentIntentId(order.get("id")); // Razorpay Order ID
                payment.setStatus("CREATED");
                payment.setCreatedAt(LocalDateTime.now());

                Payment savedPayment = paymentRepository.save(payment);

                // Prepare response
                PaymentResponseDTO response = new PaymentResponseDTO();
                response.setPaymentId(savedPayment.getId());
                response.setPaymentIntentId(order.get("id")); // Razorpay Order ID
                response.setClientSecret(order.get("id")); // Razorpay Order ID
                response.setStatus(savedPayment.getStatus());

                return response;
            }
            catch (RazorpayException e) {
 throw new PaymentProcessingException("Failed to create Razorpay order", e);
}

        }

    public void updatePaymentStatus(String paymentIntentId, String status) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with intent ID: " + paymentIntentId));
        payment.setStatus(status);
        paymentRepository.save(payment);
    }


    public Double getRateForRoomType(String roomType) {
        return roomServiceClient.getRateByRoomType(roomType);
    }
    public PaymentRequestForStatus getPaymentStatusByBookingId(String bookingId) {
        // You should retrieve the payment by booking ID from your database
        // Here's an example:
        Optional<Payment> paymentOpt = paymentRepository.findByBookingId(bookingId);
        PaymentRequestForStatus request = new PaymentRequestForStatus();
        request.setBookingId(paymentOpt.get().getBookingId());
        request.setAmount(paymentOpt.get().getAmount());
        request.setCurrency(paymentOpt.get().getCurrency());
        request.setStatus(paymentOpt.get().getStatus());
        request.setCreatedAt(paymentOpt.get().getCreatedAt());
        request.setCustomerEmail(paymentOpt.get().getCustomerEmail());
        return request;
    }

    public List<PaymentRequestForStatus> findAll() {
        List<Payment> all=paymentRepository.findAll();
        List<PaymentRequestForStatus> list = new ArrayList<>();
        for (Payment paymentOpt : all) {
            PaymentRequestForStatus request = new PaymentRequestForStatus();
            request.setBookingId(paymentOpt.getBookingId());
            request.setAmount(paymentOpt.getAmount());
            request.setCurrency(paymentOpt.getCurrency());
            request.setStatus(paymentOpt.getStatus());
            request.setCreatedAt(paymentOpt.getCreatedAt());
            request.setCustomerEmail(paymentOpt.getCustomerEmail());
            list.add(request);
        }
        return list;
    }
}
