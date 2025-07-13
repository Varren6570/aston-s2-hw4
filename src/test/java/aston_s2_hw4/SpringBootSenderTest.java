package aston_s2_hw4;

import aston_s2_hw4.kafka.KafkaSender;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Конфигурация Kafka‑продюсера.
 *
 * <p>Поднимаем контекст только для работы {@link KafkaSender}.</p>
 * <p>Используем кастомную конфигурацию {@code KafkaProducer} {@link KafkaProducerTestConfig}.</p>
 * <p>Используется тестовый application.yml.</p>
 * <p>Вместо внешней кафки используется embeddedKafka/</p>
 * <p>Топик кафки создается не в {@code KafkaTopic}, а задается в аннотации.</p>
 * <p>Используется вручную созданный consumer</p>
 */
@Import(KafkaProducerTestConfig.class)
@SpringBootTest(classes = {KafkaSender.class})
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
class SpringBootSenderTest {

    @Autowired
    EmbeddedKafkaBroker broker;

    @Autowired
    KafkaSender sender;

    Consumer<String, String> consumer;

    /**
     * Подготовка к тесту.
     *
     * <p>Получаем properties из {@link EmbeddedKafkaBroker}, по ним создаем консьюмера<p/>
     */
    @BeforeEach
    void setUp() {

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", broker);
        consumerProps.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        consumerProps.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    /**
     * Тест.
     *
     * <p>Собираем сообщение билдером, отправлеям, вычитываем из консьюмера, сверяем payload, key и topic<p/>
     */
    @Test
    public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived() throws Exception {

        Message<String> message = MessageBuilder
                .withPayload("test@test.com")
                .setHeader(KafkaHeaders.TOPIC, "test-topic")
                .setHeader(KafkaHeaders.KEY, "test@test.com")
                .setHeader("eventType", "TEST_TYPE")
                .build();

        sender.sendMessage(message);

        broker.consumeFromAnEmbeddedTopic(consumer, "test-topic");
        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "test-topic");  // один полученный рекорд

        assertEquals("test@test.com", record.value());
        assertEquals("test@test.com", record.key());
        assertEquals("test-topic", record.topic());
    }
}