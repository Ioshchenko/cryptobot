package com.cryptobot.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

@Log4j2
@Component
public class ExmoConsumer {

    @Resource(name = "kafkaProperties")
    private Properties kafkaProperties;

    public void poll() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        consumer.subscribe(Arrays.asList(kafkaProperties.getProperty("topic")));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                log.info(record.key() + " " + record.value());
            }
        }
    }
}
