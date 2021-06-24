package pl.edu.utp.mybookshelf.database;

import com.google.firebase.firestore.CollectionReference;
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

    public static void getAllBooks(FirebaseCallback callback) {
        List<Book> books = new ArrayList<>();
        bookCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Book book = new Book();
                book.setId((Long) document.get("id"));
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
            callback.getAllBooks(books);
        });
    }

//    public static void initBooks() {
//        Review r1 = new Review(1L, "Dobra książka", 5, null);
//        Review r2 = new Review(2L, "Mogłaby być lepsza", 3, null);
//        Review r3 = new Review(3L, "Nie zachwyca", 2, null);
//
//        Book hp = new Book(1L, "J.K. Rowling", "Harry Potter i Kamień Filozoficzny",
//                "Książka „Harry Potter i Kamień Filozoficzny” rozpoczyna cykl o młodym czarodzieju i jego licznych przygodach. " +
//                        "Tytułowy Harry Potter wychowywany jest przez nieprzychylnych mu ciotkę i wuja. Jego rodzice zginęli w tajemniczych " +
//                        "okolicznościach, a jedyne, co mu po nich pozostało to blizna na czole w kształcie błyskawicy. W dniu swoich " +
//                        "11. urodzin bohater dowiaduje się, że istnieje świat, o którym nie miał pojęcia", R.drawable.book_1,
//                "9877323132", 326, LocalDate.of(1997, 10, 1).toString(), new Category(1L, "Fantasy"), Arrays.asList(r1, r2, r3));
//        Book book2 = new Book("J.K. Rowling", "Harry Potter i Komnata Tajemnic", R.drawable.book_1);
//        book2.setId(2L);
//        Book book3 = new Book("J.K. Rowling", "Harry Potter i Więzień Azkabanu", R.drawable.book_1);
//        book3.setId(3L);
//        Book book4 = new Book("J.K. Rowling", "Harry Potter i Czara Ognia", R.drawable.book_1);
//        book4.setId(4L);
//        Book book5 = new Book("Adam Mickiewicz", "Pan Tadeusz", R.drawable.book_2);
//        book5.setId(5L);
//
//        bookCollection.document("1").set(hp);
//        bookCollection.document("2").set(book2);
//        bookCollection.document("3").set(book3);
//        bookCollection.document("4").set(book4);
//        bookCollection.document("5").set(book5);
//    }

}
