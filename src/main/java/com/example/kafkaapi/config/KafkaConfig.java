package com.example.kafkaapi.config;

import com.example.kafkaapi.Constant;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author grassPrince
 * @Date 2020/11/9 15:14
 * @Description kafka配置类
 **/
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${transwarp.kerberos.security}")
    private boolean securityFlag;

    @Value("${spring.kafka.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.kerberos.service.name}")
    private String saslKerberosServiceName;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    //创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
    @Bean
    public KafkaAdmin kafkaAdmin() {

        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址:   kafka的地址，不是zookeeper
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        if (securityFlag) {

            // 设置环境变量， 等同 java -jar -Djava.security.auth.login.config
            // System.setProperty("java.security.auth.login.config", "classpath:jaas.conf");
            // 当设置的 kdc格式为 kdc = p-tdhsit-mg1:1088时， jdk的环境变量设置有问题， 参考：https://www.cnblogs.com/flowerbirds/archive/2004/01/13/10124207.html
            // 建议使用编辑器设置VM的环境变量
            // System.setProperty("java.security.krb5.conf", "classpath:krb5.conf");

            props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            props.put(Constant.KAFKA_SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
            props.put(Constant.KAFKA_SASL_MECHANISM, saslMechanism);
        }

        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    //kafka客户端，在spring中创建这个bean之后可以注入并且创建topic,用于集群环境，创建对个副本
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }


    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.retries}")
    private String retries;

    @Value("${spring.kafka.producer.batch-size}")
    private String batchSize;

    @Value("${spring.kafka.producer.buffer-memory}")
    private String bufferMemory;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    private Properties getProduceProperties() {
        Properties properties = new Properties();

        // kafka 服务端的主机名和端口号
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 等待所有副本的应答
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        // 消息发送最大的尝试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        // 一批消息的处理大小
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        // 请求的延迟
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 发送缓冲区内存大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        // key 序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        // value 序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        if (securityFlag) {
            properties.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            properties.put(Constant.KAFKA_SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
            properties.put(Constant.KAFKA_SASL_MECHANISM, saslMechanism);
        }

        return properties;
    }

    //kafka生产者
    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer<>(getProduceProperties());
    }



    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

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
        // 最大拉取数量
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        // key 反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        // value 反序列化
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        if (securityFlag) {
            properties.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            properties.put(Constant.KAFKA_SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
            properties.put(Constant.KAFKA_SASL_MECHANISM, saslMechanism);
        }

        return properties;
    }

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumer<String, String>(getConsumeProperties());
    }

}

