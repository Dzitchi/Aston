package Third.proxy;

class FileServiceProxy implements FileService {

    private final RealFileService realFileService;
    private final User user;

    public FileServiceProxy(User user) {
        this.realFileService = new RealFileService();
        this.user = user;
    }

    public void deleteFile(String fileName) {
        if (user.isAdmin()) {
            realFileService.deleteFile(fileName);
        } else {
            System.out.println("Доступ запрещен для пользователя: " + user.getName());
        }
    }
}
