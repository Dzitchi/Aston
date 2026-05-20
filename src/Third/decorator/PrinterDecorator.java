package Third.decorator;

abstract class PrinterDecorator implements Printer {
    protected Printer decoratedPrinter;
    public PrinterDecorator(Printer printer) {
        this.decoratedPrinter = printer;
    }
    public void print() {
        decoratedPrinter.print();
    }
}
