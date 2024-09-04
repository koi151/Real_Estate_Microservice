//package com.koi151.payment.config;
//
//import com.koi151.payment.notification.PaymentNotificationRequest;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConfiguration {
//
//    @Bean
//    public ProducerFactory<String, PaymentNotificationRequest> producerFactory()
//    {
//        Map<String,Object> config = new HashMap<>();
//
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092"); // Kafka broker address
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serializer for key
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // serializer for value (JsonSerializer)
//        return new DefaultKafkaProducerFactory<>(config); // create kafka Producer Factory
//    }
//
//    @Bean // KafkaTemplate provide methods for sending messages to Kafka
//    public KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//}