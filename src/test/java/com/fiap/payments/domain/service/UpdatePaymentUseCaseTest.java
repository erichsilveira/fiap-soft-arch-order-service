package com.fiap.payments.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.payments.mocks.PaymentMock;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdatePaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private UpdatePaymentUseCaseImpl updatePaymentUseCase;

    @Test
    public void testPaymentWebhookWithSuccess() throws ResourceNotFoundException {
        String paymentId = "payment123";
        long paymentPrice = 10;
        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, paymentPrice);
        when(paymentRepository.getPayment(paymentId)).thenReturn(Optional.of(paymentModel));

        updatePaymentUseCase.paymentWebhook(paymentId, true);

        verify(paymentRepository).updatePayment(paymentId, PaymentStatus.APPROVED);
    }

    @Test
    public void testPaymentWebhookWithFailure() throws ResourceNotFoundException {
        String paymentId = "payment123";
        long paymentPrice = 10;
        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, paymentPrice);
        when(paymentRepository.getPayment(paymentId)).thenReturn(Optional.of(paymentModel));

        updatePaymentUseCase.paymentWebhook(paymentId, false);

        verify(paymentRepository).updatePayment(paymentId, PaymentStatus.REJECTED);
    }

    @Test
    public void testUpdatePaymentNotFound() {
        String paymentId = "paymentNotFound";
        long paymentPrice = 10;
        when(paymentRepository.getPayment(paymentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> updatePaymentUseCase.updatePayment(paymentId, PaymentStatus.APPROVED));
    }

    @Test
    public void testUpdatePaymentNotUpdatable() throws ResourceNotFoundException {
        String paymentId = "payment123";
        long paymentPrice = 10;
        PaymentModel paymentModel = PaymentMock.generatePaymentModel(paymentId, paymentPrice,
            PaymentStatus.APPROVED);
        Payment payment = PaymentModel.toPayment(paymentModel);

        when(paymentRepository.getPayment(paymentId)).thenReturn(Optional.of(paymentModel));

        assertThrows(RuntimeException.class,
            () -> updatePaymentUseCase.updatePayment(paymentId, PaymentStatus.REJECTED));
    }
}
