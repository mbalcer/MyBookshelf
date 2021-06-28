package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.edu.utp.mybookshelf.R;

public class ChangeEmailActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText newEmailText;
    private TextInputLayout newEmailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        auth = FirebaseAuth.getInstance();
        newEmailText = findViewById(R.id.change_email_text);
        newEmailLayout = findViewById(R.id.change_email_text_layout);

        newEmailText.setText(auth.getCurrentUser().getEmail());

        findViewById(R.id.change_email_button).setOnClickListener(view -> {
            newEmailText.setError(null);
            String newEmail = newEmailText.getText().toString().trim();

            if (validate(newEmail)) {
                changeEmail(newEmail);
            }
        });

    }

    private void changeEmail(String newEmail) {
        FirebaseUser user = auth.getCurrentUser();
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Poprawnie zmieniono adres email", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Wystąpił błąd podczas zmiany adresu email: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(ChangeEmailActivity.class.getName(), String.valueOf(task.getException()));
                    }
                });
    }

    private boolean validate(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmailLayout.setError(getString(R.string.incorrect_email_error));
            return false;
        }

        return true;
    }

}