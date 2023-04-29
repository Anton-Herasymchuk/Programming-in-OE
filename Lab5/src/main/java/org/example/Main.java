package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    private static final String filename = "log.txt";

    static volatile int count = 0;

    static  volatile  FileWriter fw ;


    public static void main(String[] args) throws IOException {

        fw = new FileWriter(filename);

        Thread t1 = new MyThread("First", 250, true);
        Thread t2 = new MyThread("Second", 500, false);
        Thread t3 = new MyThread("Third", 1000, false);

        t1.start();
        t2.start();
        t3.start();
    }


    private static class MyThread extends Thread {

        private final Integer delay;
        private final boolean isChangeThread;

        public MyThread(String ThreadName, int delay, boolean isChangeThread) {
            this.setName(ThreadName);
            this.delay = delay;
            this.isChangeThread = isChangeThread;
        }

        @Override
        public void run() {
            while (count < 240) {
                try {
                    fw.write(String.format("%s thread: %s; count: %d;\n", this.getName(), LocalDateTime.now(), count));
                    fw.flush();
                    if (isChangeThread) {
                        System.out.println(count);
                        count++;
                    }
                    sleep(delay);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}