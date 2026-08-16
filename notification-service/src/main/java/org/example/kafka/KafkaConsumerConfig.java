package org.example.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.dto.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory() {

        JacksonJsonDeserializer<UserEvent> deserializer =
                new JacksonJsonDeserializer<>(UserEvent.class);

        deserializer.ignoreTypeHeaders();

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        "bootstrap.servers", bootstrapServers,
                        "group.id", "notification-service",
                        "auto.offset.reset", "earliest"
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, UserEvent> consumerFactory
    ) {

        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
