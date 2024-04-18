package com.fiap.payments.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.payments.mocks.PaymentMock;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchPaymentUseCaseTest {

    @Mock
    private PaymentRepository mockRepository;

    @InjectMocks
    private static SearchPaymentUseCaseImpl useCase;

    @AfterEach
    void resetMocks() {
        reset(mockRepository);
    }

    @Test
    public void testSearchPayment_Found() throws ResourceNotFoundException {
        String paymentId = "123";
        long price = 10;

        PaymentModel mockPaymentModel = PaymentMock.generatePaymentModel(paymentId, price);
        when(mockRepository.getPayment(paymentId)).thenReturn(Optional.of(mockPaymentModel));

        Payment result = useCase.searchPayment(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(price, result.getOrderPrice().longValue());

        verify(mockRepository).getPayment(paymentId);
    }

    @Test
    public void testSearchPayment_NotFound() {
        String paymentId = "not_found";

        assertThrows(ResourceNotFoundException.class, () -> {
            when(mockRepository.getPayment(paymentId)).thenReturn(Optional.empty());
            useCase.searchPayment(paymentId);
        });
    }
}

