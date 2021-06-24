package pl.edu.utp.mybookshelf.database;

import java.util.List;

public interface FirebaseCallback<T> {

    void getAll(List<T> list);

}
