package Third.chain;

abstract class Logger {
    protected Logger nextLogger;
    public void setNextLogger(Logger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message) {
        if (canHandle(level)) {
            write(message);
        }
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }
    protected abstract boolean canHandle(int level);
    protected abstract void write(String message);
}
