package aston_s2_hw4;

import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

/** Конфигурация Kafka‑продюсера специально для тестов. */
@TestConfiguration
public class KafkaProducerTestConfig {
  /** Внедрение зависимости {@link EmbeddedKafkaBroker} */
  @Autowired EmbeddedKafkaBroker broker;

  /**
   * Фабрика Kafka‑продюсеров с настройками по умолчанию. Свойства соединения предоставляются {@link
   * EmbeddedKafkaBroker}
   *
   * @return фабрика продюсеров Kafka
   */
  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> props = KafkaTestUtils.producerProps(broker);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  /**
   * KafkaTemplate для отправки сообщений в Kafka. Такой же как и в основной конфигурации.
   *
   * @return шаблон Kafka
   */
  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
