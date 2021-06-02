package pl.edu.utp.mybookshelf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public long setBookToRead(Long bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_BOOK_ID, bookId);
        contentValues.put(BOOKS_COLUMN_STATE, BookState.TO_READ.name());
        contentValues.put(BOOKS_COLUMN_UPDATE_DATE, String.valueOf(new Date()));
        return db.insert(BOOKS_TABLE_NAME, null, contentValues);
    }

    public boolean setBookRead(Long bookId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_STATE, BookState.READ.name());
        contentValues.put(BOOKS_COLUMN_UPDATE_DATE, String.valueOf(new Date()));
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

    private static Date getDateFromString(String dateText) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
