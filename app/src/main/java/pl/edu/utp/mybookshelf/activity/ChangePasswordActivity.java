package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import pl.edu.utp.mybookshelf.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText oldPasswordText, newPasswordText, confirmNewPasswordText;
    private TextInputLayout oldPasswordLayout, newPasswordLayout, confirmNewPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();

        oldPasswordText = findViewById(R.id.old_password);
        newPasswordText = findViewById(R.id.new_password);
        confirmNewPasswordText = findViewById(R.id.confirm_new_password);
        oldPasswordLayout = findViewById(R.id.old_password_layout);
        newPasswordLayout = findViewById(R.id.new_password_layout);
        confirmNewPasswordLayout = findViewById(R.id.confirm_new_password_layout);

        findViewById(R.id.change_password_button).setOnClickListener(view -> {
            clearErrorMessages();
            String oldPassword = oldPasswordText.getText().toString().trim();
            String newPassword = newPasswordText.getText().toString().trim();
            String confirmNewPassword = confirmNewPasswordText.getText().toString().trim();

            if (validate(oldPassword, newPassword, confirmNewPassword)) {
                changePassword(newPassword);
            }
        });

    }

    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Hasło zostało zmienione", Toast.LENGTH_LONG).show();
                        clearTextFields();
                    } else {
                        Toast.makeText(this, "Wystąpił błąd podczas zmiany hasła: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(ChangePasswordActivity.class.getName(), String.valueOf(task.getException()));
                    }
                });
    }

    private boolean validate(String oldPassword, String newPassword, String confirmNewPassword) {
        boolean validate = true;

        if (TextUtils.isDigitsOnly(oldPassword)) {
            oldPasswordLayout.setError(getString(R.string.login_password_error));
            validate = false;
        }
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$");
        if (TextUtils.isEmpty(newPassword) || !PASSWORD_PATTERN.matcher(newPassword).matches()) {
            newPasswordLayout.setError(getString(R.string.register_password_error));
            validate = false;
        }
        if (TextUtils.isEmpty(confirmNewPassword)) {
            confirmNewPasswordLayout.setError(getString(R.string.register_enter_confirm_password_error));
            validate = false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            confirmNewPasswordLayout.setError(getString(R.string.register_confirm_password_error));
            validate = false;
        }

        return validate;
    }

    private void clearTextFields() {
        oldPasswordText.setText("");
        newPasswordText.setText("");
        confirmNewPasswordText.setText("");
    }

    private void clearErrorMessages() {
        oldPasswordLayout.setError(null);
        newPasswordLayout.setError(null);
        confirmNewPasswordLayout.setError(null);
    }
}