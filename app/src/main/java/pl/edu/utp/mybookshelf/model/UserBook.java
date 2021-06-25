package pl.edu.utp.mybookshelf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {

    private String bookId;
    private BookState state;

}
