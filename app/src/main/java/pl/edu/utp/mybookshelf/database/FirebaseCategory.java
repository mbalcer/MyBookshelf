package pl.edu.utp.mybookshelf.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import pl.edu.utp.mybookshelf.model.Category;

public class FirebaseCategory {

    private static final CollectionReference categoryCollection = FirebaseFirestore.getInstance().collection("categories");

    public static void getAll(FirebaseCallback<Category> callback) {
        List<Category> categories = new ArrayList<>();
        categoryCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Category category = new Category();
                category.setId((Long) document.get("id"));
                category.setName((String) document.get("name"));
                categories.add(category);
            }
            callback.getAll(categories);
        });
    }

    public static void init() {
        Category cat1 = new Category(1L, "Fantastyka");
        Category cat2 = new Category(2L, "Science Fiction");
        Category cat3 = new Category(3L, "Horror");
        Category cat4 = new Category(4L, "Klasyka");
        Category cat5 = new Category(5L, "Kryminał");
        Category cat6 = new Category(6L, "Thriller");
        Category cat7 = new Category(7L, "Literatura młodzieżowa");
        Category cat8 = new Category(8L, "Literatura dziecięca");
        Category cat9 = new Category(9L, "Literatura obyczajowa");
        Category cat10 = new Category(10L, "Romans");
        Category cat11 = new Category(11L, "Literatura piękna");
        Category cat12 = new Category(12L, "Powieść historyczna");
        Category cat13 = new Category(13L, "Powieść przygodowa");
        Category cat14 = new Category(14L, "Biografia");
        Category cat15 = new Category(15L, "Autobiografia");
        Category cat16 = new Category(16L, "Pamiętnik");
        Category cat17 = new Category(17L, "Literatura faktu");
        Category cat18 = new Category(18L, "Literatura podróżnicza");
        Category cat19 = new Category(19L, "Publicystyka literacka");
        Category cat20 = new Category(20L, "Esej");
        Category cat21 = new Category(21L, "Literatura popularnonaukowa");
        Category cat22 = new Category(22L, "Poradnik");
        Category cat23 = new Category(23L, "Komiks");
        Category cat24 = new Category(24L, "Poezja");
        Category cat25 = new Category(25L, "Dramat");
        Category cat26 = new Category(26L, "Komedia");
        Category cat27 = new Category(27L, "Tragedia");
        Category cat28 = new Category(28L, "Czasopismo");
        Category cat29 = new Category(29L, "Kulinaria");
        Category cat30 = new Category(30L, "Religia");

        categoryCollection.document("1").set(cat1);
        categoryCollection.document("2").set(cat2);
        categoryCollection.document("3").set(cat3);
        categoryCollection.document("4").set(cat4);
        categoryCollection.document("5").set(cat5);
        categoryCollection.document("6").set(cat6);
        categoryCollection.document("7").set(cat7);
        categoryCollection.document("8").set(cat8);
        categoryCollection.document("9").set(cat9);
        categoryCollection.document("10").set(cat10);
        categoryCollection.document("11").set(cat11);
        categoryCollection.document("12").set(cat12);
        categoryCollection.document("13").set(cat13);
        categoryCollection.document("14").set(cat14);
        categoryCollection.document("15").set(cat15);
        categoryCollection.document("16").set(cat16);
        categoryCollection.document("17").set(cat17);
        categoryCollection.document("18").set(cat18);
        categoryCollection.document("19").set(cat19);
        categoryCollection.document("20").set(cat20);
        categoryCollection.document("21").set(cat21);
        categoryCollection.document("22").set(cat22);
        categoryCollection.document("23").set(cat23);
        categoryCollection.document("24").set(cat24);
        categoryCollection.document("25").set(cat25);
        categoryCollection.document("26").set(cat26);
        categoryCollection.document("27").set(cat27);
        categoryCollection.document("28").set(cat28);
        categoryCollection.document("29").set(cat29);
        categoryCollection.document("30").set(cat30);
    }

}
