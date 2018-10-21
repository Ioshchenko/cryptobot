package com.cryptobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean(name = "kafkaProperties")
    public Properties kafkaProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("kafka.properties").getInputStream());
        properties.put("bootstrap.servers", System.getenv("CLOUDKARAFKA_BROKERS"));
        properties.put("sasl.jaas.config", getJaas());
        properties.put("topic", System.getenv("CLOUDKARAFKA_USERNAME") + properties.getProperty("group.id"));
        return properties;
    }

    private String getJaas() {
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        return String.format(
                jaasTemplate,
                System.getenv("CLOUDKARAFKA_USERNAME"),
                System.getenv("CLOUDKARAFKA_PASSWORD"));
    }
}
