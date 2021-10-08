package com.example.kafkaapi.service.spring;

import com.example.kafkaapi.model.ProduceMessage;
import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author grassPrince
 * @Date 2020/12/24 9:00
 * @Description spring操作的produce业务类
 **/
@Component
@Slf4j
public class SpringProduceService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 生产单条数据
    public ResponseVO produce(ProduceMessage produceMessage) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(ProduceMessage.generateRecord(produceMessage));
        CompletableFuture<SendResult<String, String>> completableFuture = listenableFuture.completable();
        SendResult<String, String> sendResult = completableFuture.get();
        RecordMetadata recordMetadata = sendResult.getRecordMetadata();

        log.info("【spring发送数据】 topic: {}, partition: {}, offset: {}, timestamp: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
        ResponseVO responseVO = ResponseVO.success("topic: " + recordMetadata.topic() + ", partition: " + recordMetadata.partition() + ", offset: " + recordMetadata.offset() + ", timestamp: " + recordMetadata.timestamp());

        return responseVO;
    }

    // 生产多条数据
    public ResponseVO produceList(List<ProduceMessage> messageList) throws ExecutionException, InterruptedException {
        ResponseVO responseVO = null;
        if(messageList.size() > 0) {
            List<Object> list = new ArrayList<>();
            for ( ProduceMessage message: messageList) {
                list.add(produce(message).getMessage());
            }
            responseVO = ResponseVO.success(list);
        } else {
            responseVO = ResponseVO.fail("待生产数据不能为空");
        }
        return responseVO;
    }
}
