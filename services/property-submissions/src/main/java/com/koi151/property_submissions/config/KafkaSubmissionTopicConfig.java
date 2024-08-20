package com.koi151.property_submissions.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaSubmissionTopicConfig {
    @Bean
    public NewTopic submissionTopic() {
        return TopicBuilder
            .name("submission-topic")
            .build();
    }

}
