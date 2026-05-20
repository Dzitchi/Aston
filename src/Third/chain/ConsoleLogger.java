package Third.chain;

class ConsoleLogger extends Logger {
    protected boolean canHandle(int level) {
        return level == 1;
    }
    protected void write(String message) {
        System.out.println("Console: " + message);
    }
}
