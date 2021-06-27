package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quote implements Serializable {

    private User user;
    private String text;
    private String page;
    private String publishTime;

    public Quote() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        publishTime = LocalDateTime.now().format(formatter);
    }

}
