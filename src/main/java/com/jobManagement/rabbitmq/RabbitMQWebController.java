package com.jobManagement.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


//@RestController
public class RabbitMQWebController {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("reached here "+file);

        rabbitMQSender.send(file);

        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}
