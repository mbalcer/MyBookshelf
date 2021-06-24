package pl.edu.utp.mybookshelf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance = null;

    public static final String DATABASE_NAME = "MyBookshelf.db";
    public static final Integer DATABASE_VERSION = 1;

    public static final String BOOKS_TABLE_NAME = "books";
    public static final String BOOKS_COLUMN_BOOK_ID = "book_id";
    public static final String BOOKS_COLUMN_STATE = "state";

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "create table " + BOOKS_TABLE_NAME + " (" +
                BOOKS_COLUMN_BOOK_ID + " integer primary key, " +
                BOOKS_COLUMN_STATE + " text not null)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropQuery = "drop table if exists " + BOOKS_TABLE_NAME;
        db.execSQL(dropQuery);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
