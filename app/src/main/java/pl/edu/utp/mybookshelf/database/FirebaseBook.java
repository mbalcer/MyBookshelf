package pl.edu.utp.mybookshelf.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Category;
import pl.edu.utp.mybookshelf.model.Review;

public class FirebaseBook {

    private static final CollectionReference bookCollection = FirebaseFirestore.getInstance().collection("books");

    public static void getAllBooks(FirebaseCallback<Book> callback) {
        List<Book> books = new ArrayList<>();
        bookCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Book book = new Book();
                book.setId(document.getId());
                book.setAuthor((String) document.get("author"));
                book.setTitle((String) document.get("title"));
                book.setDescription((String) document.get("description"));

                book.setImage((String) document.get("image"));
                book.setIsbn((String) document.get("isbn"));

                Integer pages = document.get("pages") == null ? null : ((Long) document.get("pages")).intValue();
                book.setPages(pages);

                book.setPublishDate((String) document.get("publishDate"));

                Category category = null;
                if (document.get("category") instanceof Map) {
                    HashMap<String, Object> categoryObject = (HashMap<String, Object>) document.get("category");
                    category = new Category((Long) categoryObject.get("id"), (String) categoryObject.get("name"));
                }
                book.setCategory(category);

                List<Review> reviews = new ArrayList<>();
                if (document.get("reviews") instanceof List) {
                    List<HashMap<String, Object>> reviewObjects = (List<HashMap<String, Object>>) document.get("reviews");
                    for (HashMap<String, Object> reviewObject : reviewObjects) {
                        Review review = new Review();
                        review.setId((Long) reviewObject.get("id"));
                        review.setText((String) reviewObject.get("text"));
                        Integer rating = reviewObject.get("rating") == null ? null : ((Long) reviewObject.get("rating")).intValue();
                        review.setRating(rating);
//                        TODO: user setting
//                        review.setUser();
                        reviews.add(review);
                    }
                    book.setReviews(reviews);
                }
                books.add(book);
            }
            callback.getAll(books);
        });
    }

    public static void getAllIsbn(FirebaseCallback<String> callback) {
        List<String> isbnList = new ArrayList<>();
        bookCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                isbnList.add((String) document.get("isbn"));
            }
            callback.getAll(isbnList);
        });
    }

    public static void save(Book book) {
        DocumentReference bookRef = bookCollection.document();
        bookRef.set(book);
        book.setId(bookRef.getId());
    }

}
