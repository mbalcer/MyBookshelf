package pl.edu.utp.mybookshelf.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private String id;
    private String author;
    private String title;
    private String description;
    private String image;
    private String isbn;
    private Integer pages;
    private String publishDate;

    private Category category;
    private List<Review> reviews;
    private List<Quote> quotes;

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public Book(String author, String title, String image) {
        this.author = author;
        this.title = title;
        this.image = image;
    }

    /**
     * Field excluded so as not to be saved in firebase.
     */
    @Exclude
    public String getId() {
        return id;
    }

}
