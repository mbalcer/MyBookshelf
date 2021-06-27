package pl.edu.utp.mybookshelf.service;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.AuthActivity;

public class RegisterService {
    private FirebaseAuth auth;
    private AuthActivity context;
    private RelativeLayout registerLayout;

    private TextInputEditText emailRegisterEditText, passwordRegisterEditText, confirmPasswordRegisterEditText;
    private TextInputLayout emailRegisterLayout, passwordRegisterLayout, confirmPasswordRegisterLayout;

    public RegisterService(AuthActivity context, RelativeLayout registerLayout) {
        this.context = context;
        this.registerLayout = registerLayout;
        this.auth = FirebaseAuth.getInstance();

        emailRegisterEditText = registerLayout.findViewById(R.id.emailRegisterEditText);
        passwordRegisterEditText = registerLayout.findViewById(R.id.passwordRegisterEditText);
        confirmPasswordRegisterEditText = registerLayout.findViewById(R.id.confirmPasswordRegisterEditText);

        emailRegisterLayout = registerLayout.findViewById(R.id.emailRegisterLayout);
        passwordRegisterLayout = registerLayout.findViewById(R.id.passwordRegisterLayout);
        confirmPasswordRegisterLayout = registerLayout.findViewById(R.id.confirmPasswordRegisterLayout);

        registerLayout.findViewById(R.id.registerButton).setOnClickListener(view -> {
            clearErrorMessage();
            String email = emailRegisterEditText.getText().toString().trim();
            String password = passwordRegisterEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordRegisterEditText.getText().toString().trim();

            if (validateRegister(email, password, confirmPassword)) {
                register(email, password);
            }
        });
    }

    private void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Błąd podczas rejestracji: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(RegisterService.class.getName(), String.valueOf(task.getException()));
                    } else {
                        Toast.makeText(context, "Rejestracja przebiegła pomyślnie. Teraz możesz się zalogować", Toast.LENGTH_SHORT).show();
                        clearEditText();
                    }
                });
    }

    private boolean validateRegister(String email, String password, String confirmPassword) {
        boolean validate = true;

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailRegisterLayout.setError(context.getString(R.string.register_email_error));
            validate = false;
        }
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$");
        if (TextUtils.isEmpty(password) || !PASSWORD_PATTERN.matcher(password).matches()) {
            passwordRegisterLayout.setError(context.getString(R.string.register_password_error));
            validate = false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordRegisterLayout.setError(context.getString(R.string.register_enter_confirm_password_error));
            validate = false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordRegisterLayout.setError(context.getString(R.string.register_confirm_password_error));
            validate = false;
        }

        return validate;
    }

    private void clearEditText() {
        emailRegisterEditText.setText("");
        passwordRegisterEditText.setText("");
        confirmPasswordRegisterEditText.setText("");
    }

    private void clearErrorMessage() {
        emailRegisterLayout.setError(null);
        passwordRegisterLayout.setError(null);
        confirmPasswordRegisterLayout.setError(null);
    }
}
