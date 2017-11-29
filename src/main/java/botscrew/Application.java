package botscrew;

import botscrew.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import botscrew.repository.BookService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class Application implements CommandLineRunner {
    private final BookService bookService;

    private static String[] commands = {"add", "remove", "edit", "all books"};

    @Autowired
    public Application(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        while (!(userInput = scanner.nextLine()).equals("quit")) {
            parseInput(userInput);
        }
        System.out.println("Bye!");
    }

    private void parseInput(String userInput) {
        String response;
        if (userInput.startsWith(commands[0])) {
            response = addBook(userInput);
            System.out.println(response);
        } else if (userInput.startsWith(commands[1])) {
            response = removeBook(userInput);
            System.out.println(response);
        } else if (userInput.startsWith(commands[2])) {
            response = editBook(userInput);
            System.out.println(response);
        } else if (userInput.equals(commands[3])) {
            printAllBooks();
        } else {
            System.err.println("Invalid input!");
        }
    }

    private String editBook(String s) {
        try {
            String name = s.substring(s.indexOf(" ") + 1).trim();
            Book book = chooseBook(name);

            if (book == null)
                return "There is no book with such name";

            System.out.println("Enter new name");
            Scanner sc = new Scanner(System.in);
            String newName = sc.nextLine();

            String oldName = book.getName();
            book.setName(newName);
            bookService.saveBook(book);
            return "book " + book.getAuthor() + " \""
                    + oldName + "\" was successfully edited";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }

    private String removeBook(String s) {
        try {
            String name = s.substring(s.indexOf(" ") + 1).trim();
            Book book = chooseBook(name);

            if (book == null)
                return "There is no book with such name";

            bookService.removeBook(book);
            return "book " + book.getAuthor() + " \""
                    + book.getName() + "\" was successfully removed";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }


    private Book chooseBook(String name) {
        List<Book> bookList = bookService.getBookByName(name);
        Book book;
        if (bookList.isEmpty()) {
            return null;
        } else if (bookList.size() != 1) {
            System.out.println("we have few books with such " +
                    "name please choose one by typing a number of book:");
            for (int i = 0; i < bookList.size(); i++) {
                System.out.println((i + 1) + ". " + bookList.get(i).getAuthor() +
                        " \"" + bookList.get(i).getName() + "\"");
            }
            Scanner sc = new Scanner(System.in);
            Integer index = sc.nextInt() - 1; // correcting indices bc our list starts from 0
            book = bookList.get(index);
        } else {
            book = bookList.get(0);
        }
        return book;
    }

    private String addBook(String s) {
        try {
            String author = s.substring(s.indexOf(" ") + 1, s.indexOf("\"")).trim();
            String name = s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\""));
            Book book = new Book(name, author);
            bookService.saveBook(book);
            return "book " + author + " \"" + name +
                    "\" was successfully added";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }

    private void printAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("Nothing in here!");
        } else {
            allBooks.sort(Comparator.comparing(Book::getName));
            allBooks.forEach(System.out::println);
        }
    }
}