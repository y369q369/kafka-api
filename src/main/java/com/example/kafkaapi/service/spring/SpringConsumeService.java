package com.example.kafkaapi.service.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author grassPrince
 * @Date 2020/12/24 14:43
 * @Description spring操作的consume业务类
 **/
@Slf4j
@Component
public class SpringConsumeService {

    @Autowired
    private KafkaConsumer<String, String> consumer;

    // spring封装的消费方式，不用时注调
//    @KafkaListener(topics = {"yang-test-StringSerializer1"})
    public void receive(ConsumerRecord<String, String> message) {
        log.info("【spring消费数据】 partition : {}, offset : {}, key : {}, value : {}", message.partition(), message.offset(), message.key(), message.value());
    }
}
