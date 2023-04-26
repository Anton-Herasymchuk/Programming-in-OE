package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {

    public static final int SIZE = 10000;

    public static void main(String[] args) {
        int min = 0, max = 100, sleep = 0;
        List<Integer> input1 = new ArrayList<>();
        List<Integer> input2 = new ArrayList<>(SIZE);
        List<Integer> result = new ArrayList<>(SIZE);
        Random r = new Random();

        for (int i = 0; i < SIZE; i++) {
            input1.add(i, r.nextInt(min, max));
            input2.add(i, r.nextInt(min, max));
        }

        //sleep = 0

        long time1 = System.currentTimeMillis();
        result =  defaultCalc(input1, input2, SIZE, sleep);
        System.out.printf("default calc(sleep = 0) : %s ms\n", System.currentTimeMillis() - time1);

        time1 = System.currentTimeMillis();
        result =  parallelStreamCalc(input1, input2, SIZE, sleep);
        System.out.printf("parallel calc(sleep = 0) : %s ms\n", System.currentTimeMillis() - time1);

        //sleep = 1
        sleep = 1;
        time1 = System.currentTimeMillis();
        result =  defaultCalc(input1, input2, SIZE, sleep);
        System.out.printf("default calc(sleep = 1) : %s ms\n", System.currentTimeMillis() - time1);

        time1 = System.currentTimeMillis();
        result =  parallelStreamCalc(input1, input2, SIZE, sleep);
        System.out.printf("parallel calc(sleep = 1) : %s ms\n", System.currentTimeMillis() - time1);
    }

    public static List<Integer> defaultCalc(List<Integer> arr1, List<Integer> arr2,  int size, int sleep){
        List<Integer> res = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            res.add(arr1.get(i) * arr2.get(i));
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }


    public static List<Integer> parallelStreamCalc(List<Integer> arr1, List<Integer> arr2,  int size, int sleep){
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            res.add(i);
        }
        res.parallelStream().forEach((indx) -> {
            res.set(indx, arr1.get(indx) * arr2.get(indx));
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return res;
    }
}