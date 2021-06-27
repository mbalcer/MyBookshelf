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
import pl.edu.utp.mybookshelf.model.Quote;
import pl.edu.utp.mybookshelf.model.Review;
import pl.edu.utp.mybookshelf.model.User;

public class FirebaseBook {

    private static final CollectionReference bookCollection = FirebaseFirestore.getInstance().collection("books");

    public static void getAllBooks(FirebaseCallback<Book> callback) {
        List<Book> books = new ArrayList<>();
        bookCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Book book = new Book();
                getBookDataFromDocument(book, document);
                books.add(book);
            }
            callback.getAll(books);
        });
    }

    public static void findByIsbn(FirebaseCallback<Book> callback, String isbn) {
        List<Book> books = new ArrayList<>();
        bookCollection.whereEqualTo("isbn", isbn).get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Book book = new Book();
                getBookDataFromDocument(book, document);
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

    public static void update(Book book) {
        bookCollection.document(book.getId()).set(book);
    }

    private static void getBookDataFromDocument(Book book, DocumentSnapshot document) {
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
                review.setText((String) reviewObject.get("text"));
                Double rating = reviewObject.get("rating") == null ? null : ((Double) reviewObject.get("rating"));
                review.setRating(rating.floatValue());
                review.setReviewTime((String) reviewObject.get("reviewTime"));
                User user = getUserDataFromMap(reviewObject);
                review.setUser(user);
                reviews.add(review);
            }
            book.setReviews(reviews);
        }

        List<Quote> quotes = new ArrayList<>();
        if (document.get("quotes") instanceof List) {
            List<HashMap<String, Object>> quoteObjects = (List<HashMap<String, Object>>) document.get("quotes");
            for (HashMap<String, Object> quoteObject : quoteObjects) {
                Quote quote = new Quote();
                quote.setText((String) quoteObject.get("text"));
                quote.setPage((String) quoteObject.get("page"));
                quote.setPublishTime((String) quoteObject.get("publishTime"));
                User user = getUserDataFromMap(quoteObject);
                quote.setUser(user);
                quotes.add(quote);
            }
            book.setQuotes(quotes);
        }
    }

    private static User getUserDataFromMap(HashMap<String, Object> objectMap) {
        User user = null;
        if (objectMap.get("user") instanceof Map) {
            HashMap<String, Object> userObject = (HashMap<String, Object>) objectMap.get("user");
            user = new User();
            user.setId(String.valueOf(userObject.get("id")));
            user.setEmail(String.valueOf(userObject.get("email")));
            user.setPassword(String.valueOf(userObject.get("password")));
            user.setFullName(String.valueOf(userObject.get("fullName")));
        }
        return user;
    }

}
