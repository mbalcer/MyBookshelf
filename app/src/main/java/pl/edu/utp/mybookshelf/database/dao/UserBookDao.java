package pl.edu.utp.mybookshelf.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Optional;

import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.UserBook;

@Dao
public interface UserBookDao {
    @Query("select * from user_books where bookId = :bookId LIMIT 1")
    Optional<UserBook> getUserBookByBookId(String bookId);

    @Query("select * from user_books where state = :state")
    List<UserBook> getAllByBookState(BookState state);

    @Insert
    void insert(UserBook userBook);

    @Update
    void update(UserBook userBook);

    @Query("DELETE FROM user_books WHERE bookId = :bookId")
    void deleteById(String bookId);
}
