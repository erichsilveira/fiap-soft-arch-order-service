package com.fiap.orders.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.domain.entity.OrderFulfillmentEvent;
import io.awspring.cloud.sqs.operations.SqsOperations;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderFulfillmentEventProducer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SqsOperations sqsOperations;

    private final String queueName;

    public OrderFulfillmentEventProducer(SqsOperations sqsOperations,
                                     @Value("${order.fullfilment.queue.url}") String queueName){
        this.sqsOperations = sqsOperations;
        this.queueName = queueName;
    }

    @SneakyThrows
    public void send(OrderFulfillmentEvent orderFulfillmentEvent) {

        log.info("Sending to queue {} - {}", "order-fulfillment-queue", orderFulfillmentEvent);

        sqsOperations.send(queueName, MAPPER.writeValueAsString(orderFulfillmentEvent));
        log.info("Message sent {}", orderFulfillmentEvent);
    }
}
