package dining_philosophers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiningPhilosopherDeadlockSimulation {

    public static void main(String[] args) {
        int nForks = 5;
        Fork[] forks = new Fork[nForks];
        for (int i = 0; i < nForks; i++) {
            forks[i] = new Fork(i);
        }

        int nPhilosophers = 5;
        CountDownLatch countDownLatch = new CountDownLatch(nPhilosophers);

        Philosopher[] philosophers = new Philosopher[nPhilosophers];
        for (int i = 0; i < nPhilosophers; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % nForks], countDownLatch);
        }

        ExecutorService executor = Executors.newFixedThreadPool(nPhilosophers);
        for (Philosopher philosopher : philosophers) {
            executor.submit(philosopher);
        }

        System.out.println("Waiting for all philosophers finish eating");
        boolean allPhilosophersFinishEating = false;
        try {
            allPhilosophersFinishEating = countDownLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }

        if (allPhilosophersFinishEating) {
            System.out.println("All philosophers have finished eating");
        } else {
            System.out.println("Not all philosophers have finished eating");
        }

        executor.shutdown();
    }

    private static class Philosopher implements Runnable {

        private final int index;
        private final Fork leftFork;
        private final Fork rightFork;
        private final CountDownLatch countDownLatch;

        private Philosopher(int index, Fork leftFork, Fork rightFork, CountDownLatch countDownLatch) {
            this.index = index;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                think();
                eat();
            }
        }

        void eat() {
            synchronized (leftFork) {
                System.out.println(this + ": Got " + leftFork + " as left fork");
                System.out.println(this + ": Waiting for right fork");
                synchronized (rightFork) {
                    System.out.println(this + ": Got " + rightFork + " as right fork");
                    perform("eating", 400);
                    countDownLatch.countDown();
                }
            }
        }

        void think() {
            perform("thinking", 200);
        }

        void perform(String action, long durationMillis) {
            System.out.println(this + ": Start " + action);

            try {
                Thread.sleep(durationMillis);
            } catch (InterruptedException e) {
                System.out.println(this + ": Interrupted when " + action);
                Thread.currentThread().interrupt();
            }

            System.out.println(this + ": Done " + action);
        }

        @Override
        public String toString() {
            return "Philosopher " + index;
        }
    }

    private static class Fork {

        private final int index;

        private Fork(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Fork " + index;
        }
    }

}
