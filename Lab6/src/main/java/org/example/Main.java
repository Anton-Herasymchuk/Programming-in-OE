package org.example;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        if (args[0].equals("worker")) Worker.run();
        else if (args[0].equals("consoleApp")) ConsoleApp.run();
        else System.out.println("Incorrect args)");
    }
}

