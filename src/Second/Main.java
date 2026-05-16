package Second;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("students.txt"));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                String studentName = parts[0];
                List<Book> studentBooks = new ArrayList<>();

                for (int i = 1; i < parts.length; i++) {
                    String[] bookData = parts[i].split(":");
                    String title = bookData[0];
                    int pages = Integer.parseInt(bookData[1]);
                    int year = Integer.parseInt(bookData[2]);
                    studentBooks.add(new Book(title, pages, year));
                }
                students.add(new Student(studentName, studentBooks));
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
            return;
        }

        System.out.println(
                students.stream()
                        .peek(System.out::println)
                        .map(Student::getBooks)
                        .flatMap(List::stream)
                        .sorted(Comparator.comparingInt(Book::getPages))
                        .distinct()
                        .filter(book -> book.getYear() > 2000)
                        .limit(3)
                        .map(Book::getYear)
                        .findFirst()
                        .map(year -> "Found book publication year: " + year)
                        .orElse("There is no such book")
        );
    }
}
