package org.example;

import com.rabbitmq.client.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Worker {

    public static void run() throws IOException, TimeoutException {
        System.out.println("Waiting...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("thumbnails_tasks", false, false, false, null);

        AtomicReferenceArray<String> imgInfo = new AtomicReferenceArray<>(new String[100]);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String data = new String(message.getBody(), StandardCharsets.UTF_8);
            String[] imgInf = data.split(",");
            for (int i = 0; i < 2; i++) {
                imgInfo.set(i, imgInf[i]);
            }

            System.out.println("I just received a image: " + imgInfo.get(0) + " thumbnail size: " + imgInfo.get(1) + "px");
            try {
                sendResult(imgInfo, factory, connection, channel);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
        channel.basicConsume("thumbnails_tasks", true, deliverCallback, consumerTag -> {
        });

    }

    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    private static void sendResult(AtomicReferenceArray<String> imgInfo, ConnectionFactory factory, Connection connection, Channel channel) throws IOException, TimeoutException {
        int size;


        String imgPath = imgInfo.get(0);
        System.out.println(imgPath);
        String[] path = imgPath.split(".png"); //cut .png

        size = (Integer.parseInt(imgInfo.get(1)));

        String saveFilePath = path[0] + "_" + size + ".png";
        resize(imgPath, saveFilePath, size, size);


        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare("thumbnails_results", false, false, false, null);
        channel.basicPublish("", "thumbnails_results", false, null, saveFilePath.getBytes());
    }
}
