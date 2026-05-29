package Fourth;

public class ThreadPrint {
    private static final Object monitor = new Object();
    private static boolean printOne = true;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (monitor) {
                    while (!printOne) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.print("1 ");
                    printOne = false;
                    monitor.notifyAll();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (monitor) {
                    while (printOne) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.print("2 ");
                    printOne = true;
                    monitor.notifyAll();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
