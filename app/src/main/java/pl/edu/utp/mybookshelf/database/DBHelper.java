package pl.edu.utp.mybookshelf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.UserBook;

import static pl.edu.utp.mybookshelf.database.DBHandler.*;

public class DBHelper {

    private static DBHandler dbHandler;

    public DBHelper(Context context) {
        dbHandler = DBHandler.getInstance(context);
    }

    public ArrayList<UserBook> getAll() {
        ArrayList<UserBook> list = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "select * from " + BOOKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            UserBook userBook = new UserBook();
            userBook.setBookId(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));

            list.add(userBook);
        }
        cursor.close();
        return list;
    }

    public ArrayList<UserBook> getAllByBookState(BookState state) {
        ArrayList<UserBook> list = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "select * from " + BOOKS_TABLE_NAME + " where state = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{state.name()});
        while (cursor.moveToNext()) {
            UserBook userBook = new UserBook();
            userBook.setBookId(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));

            list.add(userBook);
        }
        cursor.close();
        return list;
    }

    public UserBook getUserBookByBookId(String bookId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "select * from " + BOOKS_TABLE_NAME + " where book_id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{bookId.toString()});
        UserBook userBook = new UserBook();
        if (cursor.moveToNext()) {
            userBook.setBookId(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));
        }
        cursor.close();
        return userBook;
    }

    public long setBookAsToRead(String bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_BOOK_ID, bookId);
        contentValues.put(BOOKS_COLUMN_STATE, BookState.TO_READ.name());

        UserBook userBook = getUserBookByBookId(bookId);
        if (userBook.getBookId() == null)
            return db.insert(BOOKS_TABLE_NAME, null, contentValues);
        else
            return db.update(BOOKS_TABLE_NAME, contentValues, BOOKS_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
    }

    public boolean setBookAsRead(String bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_STATE, BookState.READ.name());

        return db.update(BOOKS_TABLE_NAME, contentValues, BOOKS_COLUMN_BOOK_ID + " = ? ",
                new String[]{bookId}) > 0;
    }

    public boolean deleteById(String id) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete(BOOKS_TABLE_NAME, BOOKS_COLUMN_BOOK_ID + " = ? ",
                new String[]{id}) > 0;
    }

    public boolean deleteAll() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete(BOOKS_TABLE_NAME, null, null) > 0;
    }

}
