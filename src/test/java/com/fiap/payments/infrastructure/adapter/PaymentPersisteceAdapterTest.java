package com.fiap.payments.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentPersistenceAdapterTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @InjectMocks
    private PaymentPersistenceAdapter paymentPersistenceAdapter;

    @Test
    public void testGetPayment_Found() {
        String paymentId = "payment123";
        Optional<PaymentModel> expected = Optional.of(new PaymentModel());
        when(paymentJpaRepository.findById(paymentId)).thenReturn(expected);

        Optional<PaymentModel> result = paymentPersistenceAdapter.getPayment(paymentId);

        assertTrue(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    public void testGetPayment_NotFound() {
        String paymentId = "paymentNotFound";
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.empty());

        Optional<PaymentModel> result = paymentPersistenceAdapter.getPayment(paymentId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCreatePayment() {
        String orderId = "order123";
        BigDecimal orderPrice = new BigDecimal("100.00");
        PaymentModel savedPaymentModel = new PaymentModel();
        when(paymentJpaRepository.save(any(PaymentModel.class))).thenReturn(savedPaymentModel);

        PaymentModel result = paymentPersistenceAdapter.createPayment(orderId, orderPrice);

        assertNotNull(result);
        verify(paymentJpaRepository).save(any(PaymentModel.class));
    }

    @Test
    public void testUpdatePayment_Exists() {
        String paymentId = "payment123";
        PaymentModel foundPaymentModel = new PaymentModel();
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.of(foundPaymentModel));
        when(paymentJpaRepository.save(foundPaymentModel)).thenReturn(foundPaymentModel);

        assertDoesNotThrow(
            () -> paymentPersistenceAdapter.updatePayment(paymentId, PaymentStatus.APPROVED));
        assertEquals(PaymentStatus.APPROVED, foundPaymentModel.getStatus());
        verify(paymentJpaRepository).save(foundPaymentModel);
    }

    @Test
    public void testUpdatePayment_NotFound() {
        String paymentId = "paymentNotFound";
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> paymentPersistenceAdapter.updatePayment(paymentId, PaymentStatus.APPROVED));
    }
}
