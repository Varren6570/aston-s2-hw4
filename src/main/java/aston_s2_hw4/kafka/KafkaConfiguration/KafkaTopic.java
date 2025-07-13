package aston_s2_hw4.kafka.KafkaConfiguration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
/**
 * Конфигурация Kafka‑топиков приложения.
 *
 */
@Configuration
public class KafkaTopic {
    /**
     * Описание топика {@code notification-topic}.
     *
     * <p>Сейчас создаётся с настройками по умолчанию:
     * одна партиция, репликация = 1 и без дополнительных
     * конфигурационных параметров.</p>
     *
     */
    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("notification-topic").build();
    }

}