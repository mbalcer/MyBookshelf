package pl.edu.utp.mybookshelf.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {

    private Long bookId;
    private BookState state;
    private LocalDateTime updateDate;

}
