package Fourth;

public class DeadLock {
    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("Поток 1: заблокировал Ресурс A");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Поток 1: ожидает Ресурс B...");
                synchronized (resourceB) {
                    System.out.println("Поток 1: захватил оба ресурса");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("Поток 2: заблокировал Ресурс B");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Поток 2: ожидает Ресурс A...");
                synchronized (resourceA) {
                    System.out.println("Поток 2: захватил оба ресурса");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

