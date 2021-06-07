package pl.edu.utp.mybookshelf.database;

import java.util.List;

import pl.edu.utp.mybookshelf.model.Book;

public interface FirebaseCallback {

    void getAllBooks(List<Book> books);

}
