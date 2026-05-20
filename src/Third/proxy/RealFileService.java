package Third.proxy;

class RealFileService implements FileService {

    public void deleteFile(String fileName) {
        System.out.println("Файл удален: " + fileName);
    }
}
