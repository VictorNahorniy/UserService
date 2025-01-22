package io.research.userservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9094");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userCreatedTopic() {
        return new NewTopic("user-created", 1, (short) 1); // topic name, partitions, replication factor
    }
}
