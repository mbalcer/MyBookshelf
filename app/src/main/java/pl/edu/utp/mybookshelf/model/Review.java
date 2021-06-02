package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;

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
    private User user;
}
