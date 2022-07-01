package com.healthcareApp.healthcareApp.config;


import com.healthcareApp.healthcareApp.entity.DiagnosisMail;
import com.healthcareApp.healthcareApp.entity.DoctorDiagnosis;
import com.healthcareApp.healthcareApp.entity.Illness;
import com.healthcareApp.healthcareApp.entity.MailingDao;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap.servers}")
    private String bootStrapServers;

    @Bean
    public ProducerFactory<String, MailingDao> producerConfiguration() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configurations);
    }



    @Bean
    public KafkaTemplate<String, MailingDao> kafkaTemplate() {
        return new KafkaTemplate<>(producerConfiguration());
    }

    @Bean
    public ProducerFactory<String, DiagnosisMail> diagnosisMailProducerFactory() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configurations);
    }


    @Bean
    public KafkaTemplate<String, DiagnosisMail> mailKafkaTemplate() {
        return new KafkaTemplate<>(diagnosisMailProducerFactory());
    }
}
