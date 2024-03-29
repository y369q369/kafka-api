package com.example.kafkaapi.service.api;

import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author grassPrince
 * @Date 2020/11/9 14:18
 * @Description admin的业务类
 **/
@Service
@Slf4j
public class AdminService {

    @Resource
    private AdminClient adminClient;

    public ResponseVO topics() {

        Set<String> topics = null;
        try {
            ListTopicsResult listTopics = adminClient.listTopics();
            KafkaFuture<Set<String>> topic2 = listTopics.names();
            topics = listTopics.names().get();
        } catch (Exception e) {
            log.error("查询所有topic失败", e);
            return ResponseVO.fail("查询所有topic失败" + e.getMessage());
        }

        log.info("查询所有topic: {}", topics);
        return ResponseVO.success(topics);
    }

    public ResponseVO createTopics(List<NewTopic> addTopics) {
        CreateTopicsResult createTopicsResult = adminClient.createTopics(addTopics);

        for(Map.Entry<String, KafkaFuture<Void>> e : createTopicsResult.values().entrySet()){
            KafkaFuture<Void> future= e.getValue();
            try {
                future.get();
                boolean success=!future.isCompletedExceptionally();
                log.info("创建状态： {} ", success);

            } catch (Exception ex) {
                log.error("topic: {} 创建失败", ex);
                return ResponseVO.fail("topic:创建失败, 错误信息： " + ex.getMessage());
            }
        }
        return ResponseVO.success("创建成功！");
    }

    public ResponseVO deleteTopics(List<String > deleteTopic) {
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(deleteTopic);

        for(Map.Entry<String, KafkaFuture<Void>> e : deleteTopicsResult.values().entrySet()){
            KafkaFuture<Void> future= e.getValue();
            try {
                future.get();
                boolean success=!future.isCompletedExceptionally();
                log.info("删除状态： {} ", success);

            } catch (Exception ex) {
                log.error("topic: {} 删除失败",  ex);
                return ResponseVO.fail("topic: 删除失败, 错误信息： " + ex.getMessage());
            }
        }
        return ResponseVO.success("删除成功！");
    }

    public ResponseVO consumerGrouperList() {
        List<String> groupIdList = null;
        try {
            ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();
            KafkaFuture<Collection<ConsumerGroupListing>> kafkaFuture = listConsumerGroupsResult.all();
            Collection<ConsumerGroupListing> consumerGroupListings = kafkaFuture.get();
            if ( consumerGroupListings != null ) {
                groupIdList = consumerGroupListings.stream().map(ConsumerGroupListing :: groupId).collect(Collectors.toList());
                return ResponseVO.success(groupIdList);
            }
        } catch (Exception e) {
            log.error("获取消费组失败", e);
            return ResponseVO.fail("获取消费组失败, 错误信息： " + e.getMessage());
        }
        return ResponseVO.success(groupIdList);
    }

}
