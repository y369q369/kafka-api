package com.example.kafkaapi.controller;

import com.example.kafkaapi.model.ResponseVO;
import com.example.kafkaapi.model.Topic;
import com.example.kafkaapi.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author grassPrince
 * @Date 2020/11/9 11:45
 * @Description 客户端
 **/
@Api(tags = {"admin操作"})
@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    // 获取所有topic
    @GetMapping("topics")
    @ApiOperation(value = "topics", notes = "获取所有topic")
    public ResponseVO topics() {
        return adminService.topics();
    }

    // 创建单分区，单副本数的多个topic(用,隔开的多个topic)
    @PostMapping("singleTopics")
    @ApiOperation(value = "createSingleTopics", notes = "创建单分区，单副本数的多个topic(用,隔开的多个topic)")
    public ResponseVO createSingleTopics(String topics) {
        if (topics != null) {
            List<NewTopic> addTopics = new ArrayList<>();
            String[] addTopicArr= topics.split(",");
            for (String topic : addTopicArr) {
                addTopics.add(new NewTopic(topic, 1, (short)1));
            }
            return adminService.createTopics(addTopics);
        }
        return ResponseVO.fail("待删除的topic不能为空");
    }

    // 创建多个可指定分区，指定副本数，指定配置的topic
    @PostMapping("multiTopics")
    @ApiOperation(value = "createMultiTopics", notes = "创建多个可指定分区，指定副本数，指定配置的topic")
    public ResponseVO createMultiTopics(@RequestBody List<Topic> topics) {
        List<NewTopic> addTopics = Topic.generateTopic(topics);
        if (addTopics != null && addTopics.size() > 0) {
            return adminService.createTopics(addTopics);
        }
        return ResponseVO.fail("待删除的topic不能为空");
    }

    // 删除topic： 用,分隔
    @DeleteMapping("topics")
    @ApiOperation(value = "deleteTopics", notes = "删除topic： 用,分隔")
    public ResponseVO deleteTopics(String deleteTopic) {
        if (deleteTopic != null) {
            String[] topics= deleteTopic.split(",");
            return adminService.deleteTopics(Arrays.asList(topics));
        }
        return ResponseVO.fail("待删除的topic不能为空");
    }

    // 获取消费组信息
    @GetMapping("consumerGrouperList")
    @ApiOperation(value = "consumerGrouperList", notes = "获取消费组信息")
    public ResponseVO consumerGrouperList() {
        return adminService.consumerGrouperList();
    }


}
