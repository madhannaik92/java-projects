import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ===== Book Class =====
class Book {
    private String id;
    private String title;
    private String author;
    private boolean isBorrowed;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public String getId() {
        return id;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow() {
        this.isBorrowed = true;
    }

    public void returnBook() {
        this.isBorrowed = false;
    }

    public void display() {
        System.out.printf("ID: %s | Title: %s | Author: %s | Borrowed: %s%n", id, title, author, isBorrowed ? "Yes" : "No");
    }
}

// ===== User Class =====
class User {
    private static final int BORROW_LIMIT = 3;

    private String userId;
    private String name;
    private List<Book> borrowedBooks;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() >= BORROW_LIMIT) {
            System.out.println("Borrowing limit reached (3 books max).");
            return false;
        }
        if (book.isBorrowed()) {
            System.out.println("Book is already borrowed.");
            return false;
        }
        borrowedBooks.add(book);
        book.borrow();
        System.out.println("Book borrowed successfully.");
        return true;
    }

    public boolean returnBook(Book book) {
        if (!borrowedBooks.contains(book)) {
            System.out.println("This book was not borrowed by you.");
            return false;
        }
        borrowedBooks.remove(book);
        book.returnBook();
        System.out.println("Book returned successfully.");
        return true;
    }

    public void viewBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books currently borrowed.");
            return;
        }
        System.out.println("Borrowed Books:");
        for (Book book : borrowedBooks) {
            book.display();
        }
    }
}

// ===== Library Class =====
class Library {
    private Map<String, Book> books;
    private Map<String, User> users;

    public Library() {
        books = new HashMap<>();
        users = new HashMap<>();
    }

    // Admin Functions
    public void addBook(Book book) {
        if (books.containsKey(book.getId())) {
            System.out.println("Book with this ID already exists.");
            return;
        }
        books.put(book.getId(), book);
        System.out.println("Book added successfully.");
    }

    public void removeBook(String bookId) {
        Book book = books.get(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (book.isBorrowed()) {
            System.out.println("Book is currently borrowed and cannot be removed.");
            return;
        }
        books.remove(bookId);
        System.out.println("Book removed successfully.");
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        System.out.println("Available Books:");
        for (Book book : books.values()) {
            book.display();
        }
    }

    // User Functions
    public void registerUser(String userId, String name) {
        if (users.containsKey(userId)) {
            System.out.println("User already registered.");
            return;
        }
        users.put(userId, new User(userId, name));
        System.out.println("User registered successfully.");
    }

    public void borrowBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        Book book = books.get(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        user.borrowBook(book);
    }

    public void returnBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        Book book = books.get(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        user.returnBook(book);
    }

    public void showUserBorrowedBooks(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        user.viewBorrowedBooks();
    }
}

// ===== Main Application =====
public class LibraryManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> addBook();
                case "2" -> removeBook();
                case "3" -> library.viewBooks();
                case "4" -> registerUser();
                case "5" -> borrowBook();
                case "6" -> returnBook();
                case "7" -> showUserBooks();
                case "8" -> {
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1–8.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Admin - Add Book");
        System.out.println("2. Admin - Remove Book");
        System.out.println("3. Admin - View All Books");
        System.out.println("4. User - Register");
        System.out.println("5. User - Borrow Book");
        System.out.println("6. User - Return Book");
        System.out.println("7. User - View Borrowed Books");
        System.out.println("8. Exit");
        System.out.print("Select an option: ");
    }

    private static void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        library.addBook(new Book(id, title, author));
    }

    private static void removeBook() {
        System.out.print("Enter Book ID to remove: ");
        String bookId = scanner.nextLine();
        library.removeBook(bookId);
    }

    private static void registerUser() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        library.registerUser(userId, name);
    }

    private static void borrowBook() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        library.borrowBook(userId, bookId);
    }

    private static void returnBook() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        library.returnBook(userId, bookId);
    }

    private static void showUserBooks() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        library.showUserBorrowedBooks(userId);
    }
}
