package io.research.userservice.producer;

import io.research.userservice.repository.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String userCreatedTopic = "user-created";

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreatedEvent(User user) {
        String message = "User created: " + user.getUsername();
        kafkaTemplate.send(userCreatedTopic, message);
    }
}
