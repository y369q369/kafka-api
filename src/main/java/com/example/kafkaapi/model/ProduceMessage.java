package com.example.kafkaapi.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author grassPrince
 * @Date 2020/12/23 14:34
 * @Description TODO
 **/
@Data
public class ProduceMessage {

    @ApiModelProperty(required = true, notes = "topic名称", example = "test")
    private String topic;

    @ApiModelProperty(required = true, notes = "topic的key", example = "k1")
    private String key;

    @ApiModelProperty(required = true, notes = "topic的value", example = "v1")
    private String value;

    /**
     * 处理单条待发送的数据
     */
    public static ProducerRecord generateRecord(ProduceMessage produceMessage) {
        return new ProducerRecord(produceMessage.getTopic(), produceMessage.getKey(), produceMessage.getValue());
    }

    /**
     * 处理单条待发送的数据
     */
    public static List<ProducerRecord> generateRecordList(List<ProduceMessage> produceMessageList) {
        List<ProducerRecord> list = new ArrayList<>();
        for (ProduceMessage produceMessage: produceMessageList) {
            list.add(generateRecord(produceMessage));
        }
        return list;
    }

}
