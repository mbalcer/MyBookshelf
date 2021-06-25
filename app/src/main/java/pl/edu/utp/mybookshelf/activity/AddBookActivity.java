package pl.edu.utp.mybookshelf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.database.FirebaseCallback;
import pl.edu.utp.mybookshelf.database.FirebaseCategory;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Category;

public class AddBookActivity extends AppCompatActivity {

    private TextInputEditText titleEditText;
    private TextInputEditText authorEditText;
    private TextInputEditText isbnEditText;
    private TextInputEditText pagesEditText;
    private TextInputEditText publishDateEditText;
    private TextInputEditText imageEditText;
    private TextInputEditText descriptionEditText;

    private MaterialDatePicker<Long> datePicker;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleEditText = findViewById(R.id.add_book_title);
        authorEditText = findViewById(R.id.add_book_author);
        pagesEditText = findViewById(R.id.add_book_pages);
        imageEditText = findViewById(R.id.add_book_image);
        descriptionEditText = findViewById(R.id.add_book_description);
        categorySpinner = findViewById(R.id.add_book_category);

        TextInputLayout isbnLayout = findViewById(R.id.add_book_isbn_layout);
        isbnEditText = findViewById(R.id.add_book_isbn);
        isbnLayout.setEndIconOnClickListener(view -> openScanner());

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("scannedIsbn") != null) {
            String isbn = getIntent().getExtras().getString("scannedIsbn");
            isbnEditText.setText(isbn);
            getIntent().removeExtra("scannedIsbn");
        }

        TextInputLayout publishDateLayout = findViewById(R.id.add_book_publish_date_layout);
        publishDateEditText = findViewById(R.id.add_book_publish_date);
        initDatePicker(publishDateEditText);

        publishDateLayout.setEndIconOnClickListener(view -> datePicker.show(getSupportFragmentManager(), AddBookActivity.class.getName()));
        publishDateEditText.setOnClickListener(view -> datePicker.show(getSupportFragmentManager(), AddBookActivity.class.getName()));

        initSpinner();
    }

    private void initDatePicker(TextInputEditText publishDateEditText) {
        datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.select_date)
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            DateTimeFormatter datePickerFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().parseLenient()
                    .appendPattern("[MMM dd, yyyy]")
                    .appendPattern("[MMM d, yyyy]")
                    .toFormatter();
            DateTimeFormatter editTextFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(datePicker.getHeaderText(), datePickerFormatter);
            publishDateEditText.setText(editTextFormatter.format(date));
        });
    }

    private void initSpinner() {
        List<Category> categories = new ArrayList<>();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        FirebaseCategory.getAll(new FirebaseCallback<Category>() {
            @Override
            public void getAll(List<Category> list) {
                list.sort(Comparator.comparing(Category::getName));
                categories.addAll(list);
                dataAdapter.notifyDataSetChanged();
            }
        });
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                Log.d(AddBookActivity.class.getName(), "Selected: " + category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button addBookButton = findViewById(R.id.add_book_button);
        addBookButton.setOnClickListener(view -> addBook());
    }

    private void addBook() {
        boolean invalidData = false;
        Editable titleEditable = titleEditText.getText();
        if (titleEditable == null || titleEditable.toString().trim().isEmpty()) {
            TextInputLayout titleLayout = findViewById(R.id.add_book_title_layout);
            titleLayout.setError("To pole jest wymagane");
            invalidData = true;
        }
        Editable authorEditable = authorEditText.getText();
        if (authorEditable == null || authorEditable.toString().trim().isEmpty()) {
            TextInputLayout authorLayout = findViewById(R.id.add_book_author_layout);
            authorLayout.setError("To pole jest wymagane");
            invalidData = true;
        }
        // TODO: checking if isbn unique
        Editable isbnEditable = isbnEditText.getText();
        if (isbnEditable == null || isbnEditable.toString().trim().isEmpty()) {
            TextInputLayout isbnLayout = findViewById(R.id.add_book_isbn_layout);
            isbnLayout.setError("To pole jest wymagane");
            invalidData = true;
        }
        if (!invalidData) {
            Book book = new Book();
            book.setTitle(titleEditable.toString());
            book.setAuthor(authorEditable.toString());
            book.setIsbn(isbnEditable.toString());

            Editable pagesEditable = pagesEditText.getText();
            if (pagesEditable != null && !pagesEditable.toString().trim().isEmpty()) {
                book.setPages(Integer.valueOf(pagesEditable.toString()));
            }
            Editable publishDateEditable = publishDateEditText.getText();
            if (publishDateEditable != null && !publishDateEditable.toString().trim().isEmpty()) {
                book.setPublishDate(publishDateEditable.toString());
            }
            Editable imageEditable = imageEditText.getText();
            if (imageEditable != null && !imageEditable.toString().trim().isEmpty()) {
                book.setImage(imageEditable.toString());
            }
            Editable descriptionEditable = descriptionEditText.getText();
            if (descriptionEditable != null && !descriptionEditable.toString().trim().isEmpty()) {
                book.setDescription(descriptionEditable.toString());
            }
            book.setCategory((Category) categorySpinner.getSelectedItem());

            FirebaseBook.save(book);
            openBookActivity(book);
            Log.d(AddBookActivity.class.getName(), "Saved new book: " + titleEditable.toString());
        }
    }

    private void openScanner() {
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        intent.putExtra("addBookActivity", true);
        startActivity(intent);
    }

    private void openBookActivity(Book book) {
        Intent intent = new Intent(getApplicationContext(), BookActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

}
