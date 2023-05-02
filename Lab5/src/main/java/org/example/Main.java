package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    private static final String filename = "log.txt";

    static volatile int count = 0;

    static volatile int lastNum = 0;

    public static void main(String[] args) throws IOException {

        FileWriter fw = new FileWriter(filename);
        fw.close();

        Thread t1 = new MyThread("First", 250, true);
        Thread t2 = new MyThread("Second", 500, false);
        Thread t3 = new MyThread("Third", 1000, false);

        t1.start();
        t2.start();
        t3.start();
    }

    private static class MyThread extends Thread {

        private final Integer delay;
        private final boolean iterationThread;

        public MyThread(String threadName, int delay, boolean iterationThread) {
            this.setName(threadName);
            this.delay = delay;
            this.iterationThread = iterationThread;
        }

        @Override
        public void run() {
            while (count <= 240) {
                try (FileWriter fw = new FileWriter(filename, true)) {
                    synchronized (this) {
                        if(count != lastNum){
                            fw.write(String.format("%s thread: %s; count: %d;\n", this.getName(), LocalDateTime.now(), count));
                            fw.flush();
                            lastNum = count;

                        }
                        if (iterationThread) {
                            count++;
                        }
                    }
                    sleep(delay);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}