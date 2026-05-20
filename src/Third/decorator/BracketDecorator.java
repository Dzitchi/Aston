package Third.decorator;

class BracketDecorator extends PrinterDecorator {
    public BracketDecorator(Printer printer) {
        super(printer);
    }
    public void print() {
        System.out.print("[");
        super.print();
        System.out.print("]");
    }
}
