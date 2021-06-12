package pl.edu.utp.mybookshelf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;

import pl.edu.utp.mybookshelf.R;

public class AddBookActivity extends AppCompatActivity {
    private MaterialDatePicker<Long> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        TextInputLayout isbnLayout = findViewById(R.id.add_book_isbn_layout);
        TextInputEditText isbnEditText = findViewById(R.id.add_book_isbn);
        isbnLayout.setEndIconOnClickListener(view -> openScanner());

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("scannedIsbn") != null) {
            String isbn = getIntent().getExtras().getString("scannedIsbn");
            isbnEditText.setText(isbn);
            getIntent().getExtras().clear();
        }

        TextInputLayout publishDateLayout = findViewById(R.id.add_book_publish_date_layout);
        TextInputEditText publishDateEditText = findViewById(R.id.add_book_publish_date);
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
        Spinner categorySpinner = findViewById(R.id.add_book_category);
        List<String> categories = Arrays.asList("Fantasy", "Horror", "Dramat"); // TODO: get categories from database

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
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
    }

    private void openScanner() {
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        intent.putExtra("addBookActivity", true);
        startActivity(intent);
    }
}
