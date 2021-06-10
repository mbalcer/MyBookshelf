package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.utp.mybookshelf.R;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private Long id;
    private String author;
    private String title;
    private String description;
    private Integer image = R.drawable.unknown_book;
    private String isbn;
    private Integer pages;
    private String publishDate;

    private Category category;
    private List<Review> reviews;

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public Book(String author, String title, Integer image) {
        this.author = author;
        this.title = title;
        this.image = image;
    }
}
