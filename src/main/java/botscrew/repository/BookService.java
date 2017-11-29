package botscrew.repository;

import botscrew.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookRepository.findAll().forEach(list::add);
        return list;
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> getBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public void removeBook(Book book) {
        bookRepository.delete(book);
    }
}