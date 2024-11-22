package com.hotelking.api;

import com.hotelking.dto.OrderRequest;
import com.hotelking.entity.Customer;
import com.hotelking.entity.Order;
import com.hotelking.entity.OrderDetail;
import com.hotelking.entity.Payment;
import com.hotelking.repository.CustomerRepository;
import com.hotelking.repository.OrderRepository;
import com.hotelking.repository.PaymentRepository;
import com.hotelking.service.SessionService;
import com.hotelking.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkout")
@PreAuthorize("isFullyAuthenticated()")
public class CheckoutRestController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SessionService session;

    @PostMapping("/submitOrder")
    public @ResponseBody
    ApiResponse submitOrder(@RequestParam("amount") int orderTotal,
                            @RequestParam("orderInfo") String orderInfo,
                            @RequestParam(value = "promoteCode", required = false) String promoteCode,
                            HttpServletRequest request) {
        try {
            OrderRequest orderDTO = new OrderRequest();
            orderDTO.setAmount((double) orderTotal);
            orderDTO.setOrderInfo(orderInfo);
            orderDTO.setPromoteCode(promoteCode);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
            return new ApiResponse(true, "Redirect to VNPay", vnpayUrl);
        } catch (Exception e) {
            return new ApiResponse(false, "Error creating VNPay order: " + e.getMessage(), null);
        }
    }

    @GetMapping("/vnpay-payment")
    public @ResponseBody ApiResponse handleVNPayPayment(HttpServletRequest request,
                                                        @RequestParam(value = "note", required = false) String note,
                                                        @RequestParam(value = "fullname", required = false) String name,
                                                        @RequestParam(value = "email", required = false) String email,
                                                        @RequestParam(value = "phone", required = false) String phone,
                                                        @RequestParam(value = "address", required = false) String address,
                                                        @RequestParam(value = "amount", required = false) String amountStr,
                                                        @RequestParam(value = "promoteCode", required = false) String promoteCode) {
        int paymentStatus = vnPayService.orderReturn(request);

        if (paymentStatus == 1) {
            try {
                String orderInfo = request.getParameter("vnp_OrderInfo");
                String paymentTime = request.getParameter("vnp_PayDate");
                String transactionId = request.getParameter("vnp_TransactionNo");
                String totalPriceStr = request.getParameter("vnp_Amount");

                Double totalPrice = Double.parseDouble(totalPriceStr) / 100;

                Customer existingCustomer = customerRepository.findCustomerByEmail(email);
                if (existingCustomer == null) {
                    Customer customer = new Customer();
                    customer.setAddress(address);
                    customer.setEmail(email);
                    customer.setFullname(name);
                    customer.setPhone(phone);
                    existingCustomer = customerRepository.save(customer);
                }

                Order order = new Order();
                OrderDetail detail = new OrderDetail();

                detail.setOrderInfo(orderInfo);
                detail.setAmount(totalPrice);
                detail.setTransactionId(transactionId);
                detail.setVnpPaymentTime(paymentTime);
                detail.setPaymentMethod("VNPAY");
                detail.setPromoteCode(promoteCode != null ? promoteCode : "Không áp dụng");
                detail.setDate(new Date());
                order.setCustomer(existingCustomer);
                detail.setNote(note);

                orderRepository.save(order);

                Payment payments = new Payment();
                payments.setMethod("COD");
                payments.setStatus(payments.getMethod().equals("COD") ? "unpaid" : "paid");
                paymentRepository.save(payments);

                return new ApiResponse(true, "Order placed successfully", null);
            } catch (Exception e) {
                return new ApiResponse(false, "Error processing VNPay payment: " + e.getMessage(), null);
            }
        } else {
            return new ApiResponse(false, "Payment failed", null);
        }
    }

    @PostMapping("/cod-payment")
    public @ResponseBody ApiResponse codPayment(@RequestParam("note") String note,
                                                @RequestParam("fullname") String name,
                                                @RequestParam("email") String email,
                                                @RequestParam("phone") String phone,
                                                @RequestParam("address") String address,
                                                @RequestParam("orderInfo") String orderInfo,
                                                @RequestParam("amount") String amountStr,
                                                @RequestParam(value = "promoteCode", required = false) String promoteCode) {
        try {
            Double orderTotal = Double.parseDouble(amountStr);

            Customer existingCustomer = customerRepository.findCustomerByEmail(email);
            if (existingCustomer == null) {
                Customer customer = new Customer();
                customer.setAddress(address);
                customer.setEmail(email);
                customer.setFullname(name);
                customer.setPhone(phone);
                existingCustomer = customerRepository.save(customer);
            }

            Order order = new Order();
            OrderDetail detail = new OrderDetail();
            detail.setOrderInfo(orderInfo);
            detail.setAmount(orderTotal);
            detail.setPromoteCode(promoteCode != null ? promoteCode : "Không áp dụng");
            detail.setDate(new Date());
            detail.setPaymentMethod("COD");
            detail.setTransactionId(UUID.randomUUID().toString());
            order.setCustomer(existingCustomer);
            detail.setNote(note);

            orderRepository.save(order);

            Payment payments = new Payment();
            payments.setMethod("COD");
            payments.setStatus(payments.getMethod().equals("COD") ? "unpaid" : "paid");
            paymentRepository.save(payments);

            return new ApiResponse(true, "Order placed successfully. Please pay upon delivery.", null);
        } catch (Exception e) {
            return new ApiResponse(false, "Error processing COD payment: " + e.getMessage(), null);
        }
    }

    public static class ApiResponse {
        private boolean success;
        private String message;
        private String redirectUrl;

        public ApiResponse(boolean success, String message, String redirectUrl) {
            this.success = success;
            this.message = message;
            this.redirectUrl = redirectUrl;
        }

        // Getters and setters
    }
}
