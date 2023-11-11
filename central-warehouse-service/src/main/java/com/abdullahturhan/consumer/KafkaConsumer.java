package com.abdullahturhan.consumer;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    private final ProductService productService;

    public KafkaConsumer(ProductService productService) {
        this.productService = productService;
    }
    @KafkaListener(topics = "${kafka.topics.threshold-topics.topic1}",groupId = "${kafka.topics.threshold-topics.consumerGroup}",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    public void kafkaListenerForWarehouse1(@Payload ProductDto productDto){

        try {
            log.info(String.format("Kafka event of warehouse1 received = %s",productDto));
            productService.lookForAndTrySendNewQuantityForWarehouse1(productDto);
        }catch (Exception e){
            log.error(String.format("Error occurred  => %s ",e.getMessage()));
        }

    }

    @KafkaListener(topics = "${kafka.topics.threshold-topics.topic2}",groupId = "${kafka.topics.threshold-topics.consumerGroup}",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    public void kafkaListenerForWarehouse2(@Payload ProductDto productDto){

        try {
            log.info(String.format("Kafka event of warehouse2 received = %s",productDto));
            productService.lookForAndTrySendNewQuantityForWarehouse2(productDto);
        }catch (Exception e){
            log.error(String.format("Error occurred  => %s ",e.getMessage()));
        }

    }
}
