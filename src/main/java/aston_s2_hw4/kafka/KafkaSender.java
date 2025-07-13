package aston_s2_hw4.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Компонент для отправки сообщений в Kafka.
 */
@Component
public class KafkaSender {
    /**
     * Внедрение kafkaTemplate через конструктор.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет сообщение в Kafka с помощью KafkaTemplate.
     */
    public void sendMessage(Message<?> message) {

        System.out.println("Sending : " + message.getPayload());
        System.out.println("--------------------------------");

        kafkaTemplate.send(message);
    }
}