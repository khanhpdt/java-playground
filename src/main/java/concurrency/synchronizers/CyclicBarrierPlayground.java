package concurrency.synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierPlayground {

    private static class Task implements Runnable {

        private final String id;
        private final CountDownLatch startCoordinator;
        private final CyclicBarrier barrier;

        Task(String id, CountDownLatch startCoordinator, CyclicBarrier barrier) {
            this.id = id;
            this.startCoordinator = startCoordinator;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(id + ": Waiting for coordinator");
                startCoordinator.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(id + ": Initiated");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(id + ": Running");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(id + ": Finish");

            System.out.println(id + ": Waiting for other tasks at the barrier");
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int nTasks = 3;

        // to make sure that all tasks will start concurrently
        CountDownLatch coordinator = new CountDownLatch(1);

        CyclicBarrier tasksBarrier = new CyclicBarrier(nTasks + 1);

        ExecutorService executor = Executors.newFixedThreadPool(nTasks);
        for (int i = 0; i < nTasks; i++) {
            executor.submit(new Task("Task" + (i + 1), coordinator, tasksBarrier));
        }

        System.out.println("All tasks submitted");
        coordinator.countDown();

        try {
            System.out.println("Waiting for the tasks at the barrier");
            tasksBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("Shutting down the executor");
        executor.shutdown();
    }
}
