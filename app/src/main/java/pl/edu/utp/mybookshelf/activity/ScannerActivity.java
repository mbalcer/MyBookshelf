package pl.edu.utp.mybookshelf.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Optional;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Book;

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Zeskanuj ISBN książki");
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Optional<Book> optionalBook = getBookByIsbn(result.getContents());
                backToSearchActivity(optionalBook);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Optional<Book> getBookByIsbn(String isbn) {
        return Optional.ofNullable(new Book("J.K. Rowling", "Harry Potter")); // TODO: getting book by isbn from database
    }

    private void backToSearchActivity(Optional<Book> book) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        if (book.isPresent()) {
            intent.putExtra("resultScanner", true);
            intent.putExtra("scannedBook", book.get());
        } else {
            intent.putExtra("resultScanner", false);
        }
        startActivity(intent);
    }
}
