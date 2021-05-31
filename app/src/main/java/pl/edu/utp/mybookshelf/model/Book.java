package pl.edu.utp.mybookshelf.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String author;
    private String title;
    private String description;
    private Integer image;
    private String isbn;
    private Integer pages;
    private LocalDate publishDate;

    private Category category;
    private List<Review> reviews;

    public Book(String author, String title, Integer image) {
        this.author = author;
        this.title = title;
        this.image = image;
    }
}
