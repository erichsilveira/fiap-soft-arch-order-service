package com.fiap.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsOperations;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SQSConfig {

    @Value("${order.payment.queue.url}")
    private String queueUrl;

    // This bean is used to receive and send messages to SQS
    @Primary
    @Bean
    public SqsOperations sqsOperations(final ObjectMapper objectMapper) {
        return SqsTemplate.builder().sqsAsyncClient(sqsAsyncClient())
                .messageConverter(sqsMessagingMessageConverter(objectMapper)).buildSyncTemplate();
    }


    // This bean is used to communicate with SQS
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder().credentialsProvider(DefaultCredentialsProvider.create())
                .endpointOverride(URI.create(queueUrl)).region(Region.US_EAST_1).build();
    }

    // This bean allows receive custom objects
    @Bean
    public SqsMessagingMessageConverter sqsMessagingMessageConverter(final ObjectMapper mapper) {

        final SqsMessagingMessageConverter queueHandlerFactory = new SqsMessagingMessageConverter();
        queueHandlerFactory.setObjectMapper(mapper);
        queueHandlerFactory.setPayloadMessageConverter(jackson2MessageConverter(mapper));
        queueHandlerFactory.setPayloadTypeHeader("contentType");
        return queueHandlerFactory;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2MessageConverter(final ObjectMapper mapper) {

        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setObjectMapper(mapper);
        converter.setSerializedPayloadClass(String.class);
        converter.setStrictContentTypeMatch(false);
        return converter;
    }

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
            SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory.builder().sqsAsyncClient(sqsAsyncClient).build();
    }
}
