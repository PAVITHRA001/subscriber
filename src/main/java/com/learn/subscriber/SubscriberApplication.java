package com.learn.subscriber;

import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@SpringBootApplication
public class SubscriberApplication {

	public static void main(String[] args) {

		SpringApplication.run(SubscriberApplication.class, args);
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare("products_queue",false,false,false,null);
			Consumer consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag,
										   Envelope envelope,
										   AMQP.BasicProperties properties,
										   byte[] body) throws IOException {
					System.out.println("body "+new String(body,"UTF-8"));
				}
			};
			channel.basicConsume("products_queue", true, consumer);
		}
		catch (IOException | TimeoutException e){
			e.printStackTrace();
		}
	}

}
