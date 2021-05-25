package com.jobUploader.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.amqp.AmqpItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class RabbitMQItemReader extends AmqpItemReader<Message> {


    @Autowired
    private final RabbitTemplate template;

    public RabbitMQItemReader(RabbitTemplate rabbitTemplate) throws IOException {
        super(rabbitTemplate);
        System.out.print("here readered");
        template = rabbitTemplate;
    }

    @Override
    public Message read() {
        try {
            Message msg = template.receive();
            return msg;
         }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}