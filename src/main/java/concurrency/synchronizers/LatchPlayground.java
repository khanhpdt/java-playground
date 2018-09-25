package concurrency.synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LatchPlayground {

    public static void main(String[] args) {
        // task3 needs to wait for the other two tasks to be finished before it can run
        CountDownLatch task3Dependencies = new CountDownLatch(2);

        // to make sure that all tasks will start concurrently
        CountDownLatch coordinator = new CountDownLatch(1);

        Runnable task1 = () -> {
            try {
                System.out.println("Task1: Waiting for coordinator");
                coordinator.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task1: Initiated");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task1: Running");

            System.out.println("Task1: Finish");
            task3Dependencies.countDown();
        };

        Runnable task2 = () -> {
            try {
                System.out.println("Task2: Waiting for coordinator");
                coordinator.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task2: Initiated");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task2: Running");

            System.out.println("Task2: Finish");
            task3Dependencies.countDown();
        };

        Runnable task3 = () -> {
            try {
                System.out.println("Task3: Waiting for coordinator");
                coordinator.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task3: Initiated");

            try {
                System.out.println("Task3: Waiting for dependencies...");
                task3Dependencies.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task3: All dependencies finish");

            System.out.println("Task3: Running");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Task3: Finish");
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(task1);
        executor.submit(task2);
        Future<?> task3Future = executor.submit(task3);

        System.out.println("All tasks submitted");
        coordinator.countDown();

        try {
            System.out.println("Waiting for task3...");
            task3Future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }

}
