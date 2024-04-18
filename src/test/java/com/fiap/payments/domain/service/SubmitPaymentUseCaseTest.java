package com.fiap.payments.domain.service;

import com.fiap.payments.mocks.PaymentMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmitPaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UpdatePaymentUseCaseImpl updatePaymentUseCase;
    @Mock
    private Logger log;

    @InjectMocks
    private SubmitPaymentUseCaseImpl submitPaymentUseCase;

    @Test
    public void testSubmitPayment() {
        String paymentId = "123";
        long price = 10;

        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, price);
        when(paymentRepository.createPayment(paymentModel.getOrderId(),
                paymentModel.getOrderPrice())).thenReturn(paymentModel);

        Payment payment = submitPaymentUseCase.submitPayment(paymentModel.getOrderId(),
                paymentModel.getOrderPrice());

        assertNotNull(payment);
        assertEquals(paymentModel.getOrderId(), payment.getOrderId());
        assertEquals(price, payment.getOrderPrice().longValue());
        verify(paymentRepository).createPayment(paymentModel.getOrderId(),
                paymentModel.getOrderPrice());
    }

    @Test
    public void testSimulateCustomerPayment() {
        String paymentId = "testPayment";
        String orderId = "testOrder";
        long price = 10;

        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, price, orderId);

        when(paymentRepository.createPayment(paymentId, BigDecimal.valueOf(price)))
                .thenReturn(paymentModel);

        try {
            submitPaymentUseCase.submitPayment(paymentId, BigDecimal.valueOf(price));

            // Use Mockito verify with timeout to account for the asynchronous execution
            verify(updatePaymentUseCase, timeout(2000)).paymentWebhook(eq(paymentId), anyBoolean());
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(), "Failed to simulate customer payment");
        }
    }

    @Test
    public void testSimulateCustomerPaymentException() throws InterruptedException {
        String paymentId = "errorPayment";
        long price = 10;
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Exception> exceptionRef = new AtomicReference<>();

        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, price);
        when(paymentRepository.createPayment(anyString(), any())).thenReturn(paymentModel);

        doAnswer(invocation -> {
            new Thread(() -> {
                try {
                    // Always throw an exception for testing purposes
                    if (true) { // This condition always true for test
                        throw new RuntimeException("Payment simulation failed");
                    }
                    updatePaymentUseCase.paymentWebhook(paymentId, false);
                } catch (RuntimeException e) {
                    exceptionRef.set(e); // Capture exception
                    latch.countDown(); // Decrease count of latch, allowing main test thread to proceed
                }
            }).start();
            return null;
        }).when(updatePaymentUseCase).paymentWebhook(eq(paymentId), anyBoolean());

        submitPaymentUseCase.submitPayment(paymentId, BigDecimal.valueOf(price));
        boolean completed = latch.await(5, TimeUnit.SECONDS); // Increase wait time if needed

        assertTrue(completed, "The test did not complete within the expected time frame.");
        assertNotNull(exceptionRef.get(), "Expected an exception but none was captured.");
        assertTrue(exceptionRef.get().getMessage().contains("Payment simulation failed"), "The exception message was not as expected.");
    }
}
