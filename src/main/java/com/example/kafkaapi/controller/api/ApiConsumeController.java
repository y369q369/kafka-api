package com.example.kafkaapi.controller.api;

import com.example.kafkaapi.model.ResponseVO;
import com.example.kafkaapi.service.api.ConsumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author grassPrince
 * @Date 2020/11/9 11:45
 * @Description 消费者
 **/
@RestController
@RequestMapping("api")
@Api(tags = {"consume操作"})
public class ApiConsumeController {

    @Autowired
    private ConsumeService consumeService;

    @GetMapping("consume")
    @ApiOperation(value = "consume", notes = "消费数据")
    public ResponseVO consume(String topic) {
        return consumeService.consume(topic);
    }

}
