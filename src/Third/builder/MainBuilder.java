package Third.builder;

public class MainBuilder {
    public static void main(String[] args) {
        Computer computer = new Computer.Builder()
                .setHdd("512 GB SSD")
                .setRam("16 GB")
                .build();

        System.out.println("Компьютер собран:");
        System.out.println("HDD: " + computer.getHdd());
        System.out.println("RAM: " + computer.getRam());
    }
}
