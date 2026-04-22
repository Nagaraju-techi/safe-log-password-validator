import java.util.*;
import java.time.LocalDate;

// Book Class
class Book {
    int id;
    String title;
    String author;
    boolean issued;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = false;
    }
}

// User Class
class User {
    int id;
    String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

// Transaction Class
class Transaction {
    int bookId;
    int userId;
    LocalDate issueDate;
    LocalDate dueDate;

    Transaction(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(7); // 7 days return
    }
}

// Main Class
public class LibraryManagement {

    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== Library Menu ======");
            System.out.println("1. Add Book");
            System.out.println("2. Register User");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Show All Books");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Book ID: ");
                    int bid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();

                    books.add(new Book(bid, title, author));
                    System.out.println("Book added successfully!");
                    break;

                case 2:
                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    users.add(new User(uid, name));
                    System.out.println("User registered!");
                    break;

                case 3:
                    System.out.print("Enter title or author: ");
                    sc.nextLine();
                    String key = sc.nextLine();

                    boolean found = false;
                    for (Book b : books) {
                        if (b.title.equalsIgnoreCase(key) || b.author.equalsIgnoreCase(key)) {
                            System.out.println(b.id + " | " + b.title + " | " + b.author + " | Issued: " + b.issued);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("No book found!");
                    }
                    break;

                case 4:
                    System.out.print("Enter Book ID and User ID: ");
                    int ib = sc.nextInt();
                    int iu = sc.nextInt();

                    boolean issuedFlag = false;
                    for (Book b : books) {
                        if (b.id == ib && !b.issued) {
                            b.issued = true;
                            transactions.add(new Transaction(ib, iu));
                            System.out.println("Book issued!");
                            issuedFlag = true;
                            break;
                        }
                    }
                    if (!issuedFlag) {
                        System.out.println("Book not available!");
                    }
                    break;

                case 5:
                    System.out.print("Enter Book ID: ");
                    int rb = sc.nextInt();

                    boolean returnFlag = false;
                    for (Book b : books) {
                        if (b.id == rb && b.issued) {
                            b.issued = false;

                            Iterator<Transaction> it = transactions.iterator();
                            while (it.hasNext()) {
                                Transaction t = it.next();
                                if (t.bookId == rb) {
                                    if (LocalDate.now().isAfter(t.dueDate)) {
                                        long lateDays = LocalDate.now().toEpochDay() - t.dueDate.toEpochDay();
                                        System.out.println("Late return! Fine: ₹" + (lateDays * 5));
                                    }
                                    it.remove();
                                    break;
                                }
                            }

                            System.out.println("Book returned!");
                            returnFlag = true;
                            break;
                        }
                    }
                    if (!returnFlag) {
                        System.out.println("Invalid Book ID!");
                    }
                    break;

                case 6:
                    if (books.isEmpty()) {
                        System.out.println("No books available!");
                    } else {
                        for (Book b : books) {
                            System.out.println(b.id + " | " + b.title + " | " + b.author + " | Issued: " + b.issued);
                        }
                    }
                    break;

                case 7:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}