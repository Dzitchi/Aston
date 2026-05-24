package Third.decorator;

public class MainDecorator {
    public static void main(String[] args) {
        Printer printer = new BracketDecorator(new SimplePrinter());

        System.out.println("Результат декоратора:");
        printer.print();
        System.out.println();
    }
}
