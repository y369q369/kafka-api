package com.example.kafkaapi.service;

import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * @Author grassPrince
 * @Date 2020/11/9 14:18
 * @Description TODO
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
}
