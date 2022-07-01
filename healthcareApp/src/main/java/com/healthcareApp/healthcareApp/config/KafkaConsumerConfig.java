package com.healthcareApp.healthcareApp.config;

import com.healthcareApp.healthcareApp.entity.DiagnosisMail;
import com.healthcareApp.healthcareApp.entity.Illness;
import com.healthcareApp.healthcareApp.entity.MailingDao;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap.servers}")
    private String bootStrapServers;

    @Bean
    public ConsumerFactory<String, MailingDao> consumerConfiguration() {
        JsonDeserializer<MailingDao> deserializer = new JsonDeserializer<>(MailingDao.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(configurations, new StringDeserializer(), deserializer);
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MailingDao> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MailingDao> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerConfiguration());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, DiagnosisMail> diagnosisMailConsumerFactory() {
        JsonDeserializer<DiagnosisMail> deserializer = new JsonDeserializer<>(DiagnosisMail.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId1");
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(configurations, new StringDeserializer(), deserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DiagnosisMail> diagnosisMailConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DiagnosisMail> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(diagnosisMailConsumerFactory());
        return factory;
    }
}
