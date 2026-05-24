package Third.chain;

public class MainChain {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        logger.setNextLogger(new FileLogger());

        System.out.println("Логирование уровня 1:");
        logger.logMessage(1, "Сообщение для консоли");

        System.out.println("\nЛогирование уровня 2:");
        logger.logMessage(2, "Сообщение для файла");
    }
}
