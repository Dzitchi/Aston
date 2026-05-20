package Third.chain;

class FileLogger extends Logger {
    protected boolean canHandle(int level) {
        return level == 2;
    }
    protected void write(String message) {
        System.out.println("File: " + message);
    }
}
