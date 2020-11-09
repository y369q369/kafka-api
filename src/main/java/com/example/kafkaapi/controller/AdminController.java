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

    // 创建topic
    @PostMapping("topics")
    @ApiOperation(value = "createTopics", notes = "创建topic")
    public ResponseVO createTopics(@RequestBody List<Topic> topics) {
        List<NewTopic> addTopics = Topic.generateTopic(topics);
        if (addTopics != null && addTopics.size() > 0) {
            return adminService.createTopics(addTopics);
        }
        return ResponseVO.fail("待删除的topic不能为空");
    }

    // 删除topic： 用,分隔
    @DeleteMapping("topics")
    public ResponseVO deleteTopics(String deleteTopic) {
        if (deleteTopic != null) {
            String[] topics= deleteTopic.split(",");
            return adminService.deleteTopics(Arrays.asList(topics));
        }
        return ResponseVO.fail("待删除的topic不能为空");
    }



}
