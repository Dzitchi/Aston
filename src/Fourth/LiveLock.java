package Fourth;

public class LiveLock {
    private static boolean resourceA_OwnedByThread1 = false;
    private static boolean resourceB_OwnedByThread2 = false;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (true) {
                resourceA_OwnedByThread1 = true;
                System.out.println("Поток 1: Взял ресурс А, нужен ресурс Б...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (resourceB_OwnedByThread2) {
                    System.out.println("Поток 1: Ресурс Б занят. Освобождаю Ресурс А...");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    resourceA_OwnedByThread1 = false;
                } else {
                    System.out.println("Поток 1: Успешно выполнил работу!");
                    break;
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                resourceB_OwnedByThread2 = true;
                System.out.println("Поток 2: Взял ресурс Б, нужен ресурс А...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (resourceA_OwnedByThread1) {
                    System.out.println("Поток 2: Ресурс А занят. Освобождаю Ресурс Б...");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    resourceB_OwnedByThread2 = false;
                } else {
                    System.out.println("Поток 2: Успешно выполнил работу!");
                    break;
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
