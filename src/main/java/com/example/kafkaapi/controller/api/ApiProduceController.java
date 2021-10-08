package com.example.kafkaapi.controller.api;

import com.example.kafkaapi.model.ProduceMessage;
import com.example.kafkaapi.model.ResponseVO;
import com.example.kafkaapi.service.api.ProduceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @Author grassPrince
 * @Date 2020/11/9 11:45
 * @Description 使用api进行produce操作
 **/
@RestController
@RequestMapping("api")
@Api(tags = {"produce操作"})
public class ApiProduceController {

    @Autowired
    private ProduceService produceService;

    @PostMapping("produce")
    @ApiOperation(value = "produce", notes = "同步生产数据")
    public ResponseVO produce(ProduceMessage produceMessage) throws ExecutionException, InterruptedException {
        return produceService.produce(produceMessage);
    }

    @PostMapping("asyncProduce")
    @ApiOperation(value = "asyncProduce", notes = "异步生产数据：不方便将生产结果返回")
    public ResponseVO asyncProduce(ProduceMessage produceMessage) throws ExecutionException, InterruptedException {
        return produceService.asyncProduce(produceMessage);
    }

}
