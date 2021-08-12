package pl.edu.utp.mybookshelf.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pl.edu.utp.mybookshelf.database.converter.BookStateConverter;
import pl.edu.utp.mybookshelf.database.dao.UserBookDao;
import pl.edu.utp.mybookshelf.model.UserBook;

@Database(entities = {UserBook.class}, exportSchema = false, version = 1)
@TypeConverters(BookStateConverter.class)
public abstract class LocalDatabase extends RoomDatabase {
    private static final String DB_NAME = "bookshelf-db";
    private static LocalDatabase instance;

    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserBookDao userBookDao();
}
