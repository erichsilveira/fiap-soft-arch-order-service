package com.fiap.payments.mocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMock {

    public static PaymentModel generatePaymentModel(String id, long price) {

        return new PaymentModel(id,
                UUID.randomUUID().toString(), BigDecimal.valueOf(price),
                PaymentStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static PaymentModel generatePaymentModel(String id, long price, PaymentStatus status) {

        return new PaymentModel(id,
                UUID.randomUUID().toString(), BigDecimal.valueOf(price), status,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static PaymentModel generatePaymentModel(String id, long price, String orderId) {

        return new PaymentModel(id, orderId, BigDecimal.valueOf(price),
                PaymentStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static Payment generatePayment(String id, long price) {
        return PaymentModel.toPayment(generatePaymentModel(id, price));
    }

    public static Payment generatePayment(String id, long price, String orderId) {
        return PaymentModel.toPayment(generatePaymentModel(id, price, orderId));
    }

}
