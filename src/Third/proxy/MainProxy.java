package Third.proxy;

public class MainProxy {

    public static void main(String[] args) {
        User admin = new User("Alice", true);
        User guest = new User("Bob", false);

        FileService adminService = new FileServiceProxy(admin);
        adminService.deleteFile("report.txt");

        FileService guestService = new FileServiceProxy(guest);
        guestService.deleteFile("secret.txt");
    }
}
