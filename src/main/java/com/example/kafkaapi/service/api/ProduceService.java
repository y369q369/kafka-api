package com.example.kafkaapi.service.api;

import com.example.kafkaapi.model.ProduceMessage;
import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author grassPrince
 * @Date 2020/12/23 16:16
 * @Description 通过api操作produce的业务类
 **/
@Service
@Slf4j
public class ProduceService {

    @Autowired
    private KafkaProducer<String, String> producer;

    // 同步发送
    public ResponseVO produce(ProduceMessage produceMessage) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> future = producer.send(ProduceMessage.generateRecord(produceMessage));
        RecordMetadata recordMetadata = future.get();
        log.info("【发送数据】 topic: {}, partition: {}, offset: {}, timestamp: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
        ResponseVO responseVO = ResponseVO.success("topic: " + recordMetadata.topic() + ", partition: " + recordMetadata.partition() + ", offset: " + recordMetadata.offset() + ", timestamp: " + recordMetadata.timestamp());
        return responseVO;
    }

    // 异步发送： 无法将生产的数据返回
    public ResponseVO asyncProduce(ProduceMessage produceMessage) {
        producer.send(ProduceMessage.generateRecord(produceMessage), new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                if (recordMetadata != null) {
                    log.info("【发送数据】 topic: {}, partition: {}, offset: {}, timestamp: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                }
            }
        });
        return ResponseVO.success("生产成功");
    }

}
