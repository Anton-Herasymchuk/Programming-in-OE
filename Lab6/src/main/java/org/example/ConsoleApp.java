package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsoleApp {

    public static void run() throws IOException, TimeoutException {
        int size;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            File imgFolder = new File("./image");

            channel.queueDeclare("thumbnails_tasks", false, false, false, null);
            for (final File fileEntry : Objects.requireNonNull(imgFolder.listFiles())) {
                String imgInfo;

                imgInfo = fileEntry.getPath();
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the thumbnail size(px) for (" + fileEntry.getName() + "):");
                size = scanner.nextInt();
                imgInfo += "," + size;

                channel.basicPublish("", "thumbnails_tasks", false, null, imgInfo.getBytes());

                System.out.println("Image " + fileEntry.getName() + " has been sent !!!");
            }

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("thumbnails_results", false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String path = new String(message.getBody(), StandardCharsets.UTF_8);

            System.out.println("Resized image path: " + path);
        };
        channel.basicConsume("thumbnails_results", true, deliverCallback, consumerTag -> {
        });


    }
}
