package com.cryptobot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Component
public class KafkaConector {

    @Resource(name = "kafkaProperties")
    private Properties kafkaProperties;

    public void consume() {
        Thread thread = new Thread(() -> {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
            consumer.subscribe(Arrays.asList(kafkaProperties.getProperty("topic")));
            for (int i = 0; i < 10; i++) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("%s [%d] offset=%d, key=%s, value=\"%s\"\n",
                            record.topic(), record.partition(),
                            record.offset(), record.key(), record.value());
                }
            }
        });
        thread.start();

    }

    public void produce() {
        Thread one = new Thread(() -> {
            try {
                Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);
                for (int i = 0; i < 10; i++) {
                    Date d = new Date();
                    producer.send(new ProducerRecord<>(kafkaProperties.getProperty("topic"), Integer.toString(i), d.toString()));
                    Thread.sleep(500);
                    i++;
                }
            } catch (InterruptedException v) {
                System.out.println(v);
            }
        });
        one.start();
    }
}
