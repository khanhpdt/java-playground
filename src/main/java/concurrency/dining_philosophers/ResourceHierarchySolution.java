package concurrency.dining_philosophers;

/**
 * Using the Resource hierarchy solution. This solution occasionally suffers from starvation.
 * @see <a href="https://en.wikipedia.org/wiki/Dining_philosophers_problem">Dining_philosophers_problem</a>
 */
public class ResourceHierarchySolution {

    /*
     * Run this a few times until you see that some philosophers have not eaten.
     */
    public static void main(String[] args) {
        int nPhilosophers = 5;

        Fork[] forks = DiningPhilosophersSimulation.createForks(nPhilosophers);

        ResourceHierarchyPhilosopher[] philosophers = new ResourceHierarchyPhilosopher[nPhilosophers];
        for (int i = 0; i < nPhilosophers; i++) {
            philosophers[i] = new ResourceHierarchyPhilosopher(i, forks[i], forks[(i + 1) % forks.length]);
        }

        DiningPhilosophersSimulation.run(philosophers);
    }

    static class ResourceHierarchyPhilosopher extends Philosopher {

        private Fork firstFork;

        ResourceHierarchyPhilosopher(int index, Fork leftFork, Fork rightFork) {
            super(index, leftFork, rightFork);
        }

        @Override
        Fork pickFirstFork() {
            firstFork = leftFork.getIndex() <= rightFork.getIndex() ? leftFork : rightFork;
            return firstFork;
        }

        @Override
        Fork pickSecondFork() {
            return firstFork == leftFork ? rightFork : leftFork;
        }
    }
}
