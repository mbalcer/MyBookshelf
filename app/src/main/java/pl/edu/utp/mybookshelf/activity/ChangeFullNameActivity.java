package pl.edu.utp.mybookshelf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import pl.edu.utp.mybookshelf.R;

public class ChangeFullNameActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText fullNameText;
    private TextInputLayout fullNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_full_name);

        auth = FirebaseAuth.getInstance();
        fullNameText = findViewById(R.id.change_full_name_text);
        fullNameLayout = findViewById(R.id.change_full_name_text_layout);

        if (auth.getCurrentUser().getDisplayName() != null) {
            fullNameText.setText(auth.getCurrentUser().getDisplayName());
        }

        findViewById(R.id.change_full_name_button).setOnClickListener(view -> {
            fullNameLayout.setError(null);
            String fullName = fullNameText.getText().toString().trim();

            if (validate(fullName)) {
                changeFullName(fullName);
            }
        });

    }

    private void changeFullName(String fullName) {
        FirebaseUser user = auth.getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Poprawnie zmieniono imię i nazwisko", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Wystąpił błąd podczas zmiany danych: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(ChangeFullNameActivity.class.getName(), String.valueOf(task.getException()));
                    }
                });
    }

    private boolean validate(String fullName) {
        if (TextUtils.isEmpty(fullName)) {
            fullNameLayout.setError(getString(R.string.empty_full_name_error));
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 2);
        startActivity(intent);
        finish();
    }
}