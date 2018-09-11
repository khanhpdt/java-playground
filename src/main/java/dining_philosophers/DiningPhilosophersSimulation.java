package dining_philosophers;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DiningPhilosophersSimulation {

    static Fork[] createForks(int nForks) {
        Fork[] forks = new Fork[nForks];
        for (int i = 0; i < nForks; i++) {
            forks[i] = new Fork(i);
        }
        return forks;
    }
    
    static void run(Philosopher[] philosophers) {
        int nPhilosophers = philosophers.length;
        CountDownLatch countDownLatch = new CountDownLatch(nPhilosophers);

        for (Philosopher philosopher : philosophers) {
            philosopher.setCountDownLatch(countDownLatch);
        }

        ExecutorService executor = Executors.newFixedThreadPool(nPhilosophers);
        for (Philosopher philosopher : philosophers) {
            executor.submit(philosopher);
        }

        System.out.println("Waiting for all philosophers finish eating");

        try {
            countDownLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }

        executor.shutdownNow();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }

        if (countDownLatch.getCount() == nPhilosophers) {
            System.out.println("Deadlock has occurred");
        } else {
            long nPhilosophersHaveAte = Arrays.stream(philosophers).filter(p -> p.getEatingCount() > 0).count();
            if (nPhilosophersHaveAte == nPhilosophers) {
                System.out.println("All philosophers have ate");
            } else {
                long nPhilosophersHaveAteMoreThanOneTime = Arrays.stream(philosophers).filter(p -> p.getEatingCount() > 1).count();
                System.out.println(nPhilosophersHaveAteMoreThanOneTime + " philosophers have ate more than one time.");
                System.out.println((nPhilosophers - nPhilosophersHaveAte) + " philosophers have not ate. " +
                        "This is a symptom of starvation.");
            }
        }
    }
    
}
