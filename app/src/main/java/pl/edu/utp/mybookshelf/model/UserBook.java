package pl.edu.utp.mybookshelf.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "user_books")
public class UserBook {
    @PrimaryKey
    @NonNull
    private String bookId;
    private BookState state;
}
