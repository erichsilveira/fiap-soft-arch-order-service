package com.fiap.orders.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.domain.entity.OrderFulfillmentEvent;
import com.fiap.orders.domain.entity.OrderPaymentEvent;
import com.fiap.orders.producer.OrderFulfillmentEventProducer;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class OrderPaymentEventConsumer {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    private final OrderFulfillmentEventProducer orderFulfillmentEventProducer;

    @SqsListener(value = "${order.payment.queue.url}", acknowledgementMode = "ALWAYS")
    public void consumePaymentEvent(final String rawMessage) {
        log.info("Processing order payment event: {}", rawMessage);

        try {
            String deserializable = MAPPER.readValue(rawMessage, String.class);
            OrderPaymentEvent orderPaymentEvent = MAPPER.readValue(deserializable, OrderPaymentEvent.class);
            log.info("Deserialized Payment event: {}", orderPaymentEvent);

            if(orderPaymentEvent.getStatus().equals("APPROVED")) {
                log.info("Order payment was approved. Sending order to production");
                orderFulfillmentEventProducer.send(new OrderFulfillmentEvent(orderPaymentEvent.getOrderId(), "READY"));
            } else {
                log.info("Order payment was not approved. Notifying customer");
            }

        } catch (Exception e) {
            log.error("Error consuming payment event", e);
        }

    }
}
