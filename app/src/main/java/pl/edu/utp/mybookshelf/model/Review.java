package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review implements Serializable {
    private Long id;
    private String text;
    private Integer rating;
    private LocalDateTime reviewTime = LocalDateTime.now();
    private User user;
}
