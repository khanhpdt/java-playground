package playgrounds;

public class WaitNotify {

    public static void main(String[] args) {
        Object lock = new Object();

        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1 starts");
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread 1 interrupted");
                    e.printStackTrace();
                }
            }

            System.out.println("Thread 1 wakes up");
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            System.out.println("Thread 2 starts");

            System.out.println("Thread 2 now sleeps for 1 sec");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread 2 interrupted");
                e.printStackTrace();
            }

            synchronized (lock) {
                System.out.println("Thread 2 notifies");
                lock.notify();
            }
        });
        t2.start();
    }

}
