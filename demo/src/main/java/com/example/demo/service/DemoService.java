package com.example.demo.service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemoService {

    @Value("${asb.connection}")
    String connectionString;

    @Value("${asb.queue}")
    String queueName;

    public void sendMessage() {
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
            .connectionString(connectionString)
            .sender()
            .queueName(queueName)
            .buildClient();

        senderClient.sendMessage(new ServiceBusMessage("Hello, World!"));
        log.info("Sent a single message to the queue: {}", queueName);
        senderClient.close();
    }

    public void deadMessage() {
        ServiceBusReceiverClient  client = new ServiceBusClientBuilder()
            .connectionString(connectionString)
            .receiver()
            .queueName(queueName)
            .buildClient();
            
        client.receiveMessages(1).stream().forEach(m -> {
            client.deadLetter(m);
        });

        client.close();
    }
    
}