package Second;

import java.util.List;

class Student {
    private final String name;
    private final List<Book> books;

    public Student(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public String toString() {
        return "Student: " + name + ", Book: " + books;
    }
}
