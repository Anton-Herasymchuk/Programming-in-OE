package org.example;


import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Яке число послідовності Фіабоначчі знайти ? \n-> ");

        int n = scanner.nextInt();

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> fibonacci(n));

        do{
            System.out.println("Очікування");
            Thread.sleep(500);
        }while(!future.isDone());

        future.thenAccept(result -> {
            System.out.println("n-те число послідовності Фібоначчі: " + result);
        });
    }

    public static int fibonacci(int num) {
        if (num <= 1) return num;
        return fibonacci(num - 1) + fibonacci(num - 2); 
    }
}
