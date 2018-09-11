package playgrounds;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DeadlockSimulation {

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }

                System.out.println("Thread 1 waiting for lock 2");
                synchronized (lock2) {
                    System.out.println("Thread 1 acquires two locks");
                    countDownLatch.countDown();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }

                System.out.println("Thread 2 waiting for lock 1");
                synchronized (lock1) {
                    System.out.println("Thread 2 acquires two locks");
                    countDownLatch.countDown();
                }
            }
        });

        thread1.start();
        thread2.start();

        boolean done = false;
        try {
            System.out.println("Waiting for two threads to finish");
            done = countDownLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }

        if (done) {
            System.out.println("Two threads done");
        } else {
            System.out.println(countDownLatch.getCount() + " threads not done");
        }
    }

}
