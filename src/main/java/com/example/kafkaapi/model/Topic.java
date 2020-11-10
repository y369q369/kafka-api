package com.example.kafkaapi.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author grassPrince
 * @Date 2020/11/9
 * @Description topic对象
 **/
@Data
public class Topic {

    /** topic名称 */
    @ApiModelProperty(required = true, notes = "topic名称", example = "test")
    private String name;

    /** 分区数 */
    @ApiModelProperty(required = true, notes = "分区数，默认值为1", example = "1")
    private Integer partitionNum = 1;

    /** 副本数 */
    @ApiModelProperty(required = true, notes = "副本数， 默认值1", example = "1")
    private Short replicationNum = 1;

    /** topic配置 */
    @ApiModelProperty(required = false, notes = "topic配置", dataType = "java.util.HashMap")
    private Map<String, String> configs;

    public static List<NewTopic> generateTopic(Topic topic) {
        List<Topic> topics = Arrays.asList(topic);
        return generateTopic(topics);
    }

    public static List<NewTopic> generateTopic(List<Topic> topics) {
        List<NewTopic> newTopics = new ArrayList<>();
        if (topics != null && topics.size() > 0) {
            for (Topic topic : topics) {
                NewTopic newTopic = new NewTopic(topic.getName(), topic.getPartitionNum(), topic.getReplicationNum());
                if (topic.getConfigs() != null && topic.getConfigs().size() > 0) {
                    newTopic.configs(topic.getConfigs());
                }
                newTopics.add(newTopic);
            }
        }
        return newTopics;
    }

}
