package com.example.kafkaapi.controller;

import com.example.kafkaapi.model.ResponseVO;
import com.example.kafkaapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author grassPrince
 * @Date 2020/11/9 11:45
 * @Description 客户端
 **/
@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    // 获取所有topic
    @GetMapping("topics")
    public ResponseVO topics() {
        return adminService.topics();
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
