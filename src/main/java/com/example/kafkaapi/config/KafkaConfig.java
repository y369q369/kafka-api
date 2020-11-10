package com.example.kafkaapi.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
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

    @Value("${spring.kafka.security}")
    private boolean securityFlag;

    //创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
    @Bean
    public KafkaAdmin kafkaAdmin() {
        // 设置环境变量， 等同 java -jar -Djava.security.auth.login.config
        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址:   kafka的地址，不是zookeeper
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        if (securityFlag) {
            System.setProperty("java.security.auth.login.config", "classpath:jaas.conf");
//            Properties properties = System.getProperties();
            System.setProperty("java.security.krb5.conf", "classpath:krb5.conf");
            props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            props.put("sasl.kerberos.service.name", "kafka");
        }

        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    //kafka客户端，在spring中创建这个bean之后可以注入并且创建topic,用于集群环境，创建对个副本
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

}

