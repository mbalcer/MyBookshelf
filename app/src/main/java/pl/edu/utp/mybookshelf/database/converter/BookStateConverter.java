package pl.edu.utp.mybookshelf.database.converter;

import androidx.room.TypeConverter;

import pl.edu.utp.mybookshelf.model.BookState;

public class BookStateConverter {
    /**
     * Convert BookState to an integer
     */
    @TypeConverter
    public static int convertBookStateToInt(BookState value) {
        return value.ordinal();
    }

    /**
     * Convert an integer to Health
     */
    @TypeConverter
    public static BookState convertIntToBookState(int value) {
        return (BookState.values()[value]);
    }
}
