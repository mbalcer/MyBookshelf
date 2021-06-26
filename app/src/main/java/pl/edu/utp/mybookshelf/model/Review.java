package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review implements Serializable {

    private User user;
    private String text;
    private Float rating;
    private String reviewTime;

    public Review() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reviewTime = LocalDateTime.now().format(formatter);
    }

}
