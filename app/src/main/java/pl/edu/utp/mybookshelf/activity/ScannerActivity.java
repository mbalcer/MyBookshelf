package pl.edu.utp.mybookshelf.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.database.FirebaseCallback;
import pl.edu.utp.mybookshelf.model.Book;

public class ScannerActivity extends AppCompatActivity {
    boolean parentClassIsAddBookActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("addBookActivity") != null) {
            parentClassIsAddBookActivity = getIntent().getExtras().getBoolean("addBookActivity");
            getIntent().removeExtra("addBookActivity");
        }

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Zeskanuj ISBN książki");
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String isbn = result.getContents();
            if (isbn != null) {
                if (parentClassIsAddBookActivity) {
                    backToAddBookActivity(isbn);
                } else {
                    backToSearchActivity(isbn);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void backToSearchActivity(String isbn) {
        FirebaseBook.findByIsbn(new FirebaseCallback<Book>() {
            @Override
            public void getAll(List<Book> list) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                if (!list.isEmpty()) {
                    intent.putExtra("resultScanner", true);
                    intent.putExtra("scannedBook", list.get(0));
                } else {
                    intent.putExtra("resultScanner", false);
                }
                startActivity(intent);
            }
        }, isbn);
    }

    private void backToAddBookActivity(String isbn) {
        Intent intent = new Intent(getBaseContext(), AddBookActivity.class);
        intent.putExtra("scannedIsbn", isbn);
        startActivity(intent);
    }

}
