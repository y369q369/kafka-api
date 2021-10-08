package com.example.kafkaapi.service.api;

import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Author grassPrince
 * @Date 2020/12/23 16:20
 * @Description consume的业务类
 **/
@Service
@Slf4j
public class ConsumeService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Value("${transwarp.kerberos.security}")
    private boolean securityFlag;

    private Properties getConsumeProperties() {
        Properties properties = new Properties();

        // kafka 服务端的主机名和端口号
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 消费组id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // 是否自动确认offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        // 消费的模式，从头消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        // key 反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        // value 反序列化
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        if (securityFlag) {
            properties.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            properties.put("sasl.kerberos.service.name", "kafka");
            properties.put("sasl.mechanism", "GSSAPI");
        }

        return properties;
    }

    @Autowired
    private KafkaConsumer<String, String> consumer;

    // 消费数据
    public ResponseVO consume(String topic) {

        List<String> list = new ArrayList<>();

        // 消费者订阅的topic, 可同时订阅多个
        consumer.subscribe(Arrays.asList(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

        for (ConsumerRecord<String, String> record : records) {
            log.info("【消费数据】 partition : {}, offset : {}, key : {}, value : {}", record.partition(), record.offset(), record.key(), record.value());
            list.add("partition : " + record.partition() + ", offset : " + record.offset() + ", key : " + record.key() + ", value : " + record.value());
        }

        return ResponseVO.success(list);
    }
}
