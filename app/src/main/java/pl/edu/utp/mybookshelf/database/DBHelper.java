package pl.edu.utp.mybookshelf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.UserBook;

import static pl.edu.utp.mybookshelf.database.DBHandler.*;

public class DBHelper {

    private static DBHandler dbHandler;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
            userBook.setBookId(cursor.getLong(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));

            String dateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_UPDATE_DATE));
            userBook.setUpdateDate(getDateFromString(dateText));

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
            userBook.setBookId(cursor.getLong(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));

            String dateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_UPDATE_DATE));
            userBook.setUpdateDate(getDateFromString(dateText));

            list.add(userBook);
        }
        cursor.close();
        return list;
    }

    public UserBook getUserBookByBookId(Long bookId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "select * from " + BOOKS_TABLE_NAME + " where book_id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{bookId.toString()});
        UserBook userBook = new UserBook();
        if (cursor.moveToNext()) {
            userBook.setBookId(cursor.getLong(cursor.getColumnIndex(BOOKS_COLUMN_BOOK_ID)));

            String stateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATE));
            userBook.setState(BookState.valueOf(stateText));

            String dateText = cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_UPDATE_DATE));
            userBook.setUpdateDate(getDateFromString(dateText));
        }
        cursor.close();
        return userBook;
    }

    public long setBookAsToRead(Long bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_BOOK_ID, bookId);
        contentValues.put(BOOKS_COLUMN_STATE, BookState.TO_READ.name());
        contentValues.put(BOOKS_COLUMN_UPDATE_DATE, getStringFromDate(LocalDateTime.now()));

        UserBook userBook = getUserBookByBookId(bookId);
        if (userBook.getBookId() == null)
            return db.insert(BOOKS_TABLE_NAME, null, contentValues);
        else
            return db.update(BOOKS_TABLE_NAME, contentValues, BOOKS_COLUMN_BOOK_ID + " = ? ", new String[]{Long.toString(bookId)});
    }

    public boolean setBookAsRead(Long bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_STATE, BookState.READ.name());
        contentValues.put(BOOKS_COLUMN_UPDATE_DATE, getStringFromDate(LocalDateTime.now()));

        return db.update(BOOKS_TABLE_NAME, contentValues, BOOKS_COLUMN_BOOK_ID + " = ? ",
                new String[]{Long.toString(bookId)}) > 0;
    }

    public boolean deleteById(Long id) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete(BOOKS_TABLE_NAME, BOOKS_COLUMN_BOOK_ID + " = ? ",
                new String[]{Long.toString(id)}) > 0;
    }

    public boolean deleteAll() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete(BOOKS_TABLE_NAME, null, null) > 0;
    }

    private static String getStringFromDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }

    private static LocalDateTime getDateFromString(String dateText) {
        return LocalDateTime.parse(dateText, DATE_FORMATTER);
    }

}
