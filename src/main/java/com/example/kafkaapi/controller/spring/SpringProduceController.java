package com.example.kafkaapi.controller.spring;

import com.example.kafkaapi.model.ProduceMessage;
import com.example.kafkaapi.model.ResponseVO;
import com.example.kafkaapi.service.spring.SpringProduceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author grassPrince
 * @Date 2020/12/24 8:52
 * @Description spring封装的发送消息
 **/
@RestController
@RequestMapping("spring")
@Api(tags = {"produce操作"})
public class SpringProduceController {

    @Autowired
    private SpringProduceService kafkaProducer;

    @PostMapping("produceOne")
    @ApiOperation(value = "produceOne", notes = "生产单条数据")
    public ResponseVO produceOne(@RequestBody ProduceMessage produceMessage) throws ExecutionException, InterruptedException {
        return kafkaProducer.produce(produceMessage);
    }

    @PostMapping("produceList")
    @ApiOperation(value = "produceList", notes = "生产多条数据")
    public ResponseVO produceList( @RequestBody List<ProduceMessage> messageList) throws ExecutionException, InterruptedException {
        return kafkaProducer.produceList(messageList);
    }

}
