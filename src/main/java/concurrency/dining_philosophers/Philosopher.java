package concurrency.dining_philosophers;

import java.util.concurrent.CountDownLatch;

abstract class Philosopher implements Runnable {

    final Fork leftFork;
    final Fork rightFork;

    private final int index;

    private CountDownLatch countDownLatch;
    private int eatingCount = 0;

    Philosopher(int index, Fork leftFork, Fork rightFork) {
        this.index = index;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    int getEatingCount() {
        return eatingCount;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void eat() throws InterruptedException {
        Fork firstFork = pickFirstFork();
        synchronized (firstFork) {
            System.out.println(this + ": Got " + firstFork + " as left fork");
            System.out.println(this + ": Waiting for right fork");

            Fork secondFork = pickSecondFork();
            synchronized (secondFork) {
                System.out.println(this + ": Got " + secondFork + " as right fork");

                perform("eating", 600);

                eatingCount++;

                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }
    }

    abstract Fork pickFirstFork();

    abstract Fork pickSecondFork();

    private void think() throws InterruptedException {
        perform("thinking", 200);
    }

    private void perform(String action, long durationMillis) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        System.out.println(this + ": Start " + action);

        try {
            Thread.sleep(durationMillis);
            System.out.println(this + ": Done " + action);
        } catch (InterruptedException e) {
            System.out.println(this + ": Interrupted when " + action);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + index;
    }

}
