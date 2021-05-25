package com.jobUploader.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${javainuse.rabbitmq.exchange}")
    private String exchange;

    @Value("${javainuse.rabbitmq.routingkey}")
    private String routingkey;

    public void send(MultipartFile file) throws IOException {
        byte[] fileData = file.getBytes();
        Message message = MessageBuilder.withBody(fileData).setHeader("ContentType", "application/vnd.ms-excel").build();
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
        System.out.println("Send msg = " + message);

    }
}

