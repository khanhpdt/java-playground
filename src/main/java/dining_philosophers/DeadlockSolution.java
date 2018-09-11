package dining_philosophers;

/**
 * This solution occasionally suffers from deadlock.
 */
public class DeadlockSolution {

    /*
     * Run this a few times until you see a deadlock
     */
    public static void main(String[] args) {
        int nPhilosophers = 5;

        Fork[] forks = DiningPhilosophersSimulation.createForks(nPhilosophers);

        SimplePhilosopher[] philosophers = new SimplePhilosopher[nPhilosophers];
        for (int i = 0; i < nPhilosophers; i++) {
            philosophers[i] = new SimplePhilosopher(i, forks[i], forks[(i + 1) % forks.length]);
        }

        DiningPhilosophersSimulation.run(philosophers);
    }

    static class SimplePhilosopher extends Philosopher {

        SimplePhilosopher(int index, Fork leftFork, Fork rightFork) {
            super(index, leftFork, rightFork);
        }

        @Override
        Fork pickFirstFork() {
            return leftFork;
        }

        @Override
        Fork pickSecondFork() {
            return rightFork;
        }
    }
}
