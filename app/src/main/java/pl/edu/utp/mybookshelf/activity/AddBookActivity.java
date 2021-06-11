package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import pl.edu.utp.mybookshelf.R;

public class AddBookActivity extends AppCompatActivity {
    private MaterialDatePicker<Long> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        TextInputLayout publishDateLayout = findViewById(R.id.add_book_publish_date_layout);
        TextInputEditText publishDateEditText = findViewById(R.id.add_book_publish_date);
        initDatePicker(publishDateEditText);

        publishDateLayout.setEndIconOnClickListener(view -> datePicker.show(getSupportFragmentManager(), AddBookActivity.class.getName()));
        publishDateEditText.setOnClickListener(view -> datePicker.show(getSupportFragmentManager(), AddBookActivity.class.getName()));
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
}
