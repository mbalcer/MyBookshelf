package pl.edu.utp.mybookshelf.service;

import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.AuthActivity;

public class LoginService {
    private FirebaseAuth auth;
    private AuthActivity context;
    private RelativeLayout loginLayout;

    private TextInputEditText emailLoginEditText, passwordLoginEditText;
    private TextInputLayout emailLoginLayout, passwordLoginLayout;

    public LoginService(AuthActivity context, RelativeLayout loginLayout) {
        this.context = context;
        this.loginLayout = loginLayout;
        this.auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            context.openMainActivity();
        }

        emailLoginEditText = loginLayout.findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = loginLayout.findViewById(R.id.passwordLoginEditText);

        emailLoginLayout = loginLayout.findViewById(R.id.emailLoginLayout);
        passwordLoginLayout = loginLayout.findViewById(R.id.passwordLoginLayout);

        loginLayout.findViewById(R.id.loginButton).setOnClickListener(view -> {
            clearErrorMessage();
            String email = emailLoginEditText.getText().toString().trim();
            String password = passwordLoginEditText.getText().toString().trim();

            if (validateLogin(email, password)) {
                login(email, password);
            }
        });
    }

    private boolean validateLogin(String email, String password) {
        boolean validate = true;

        if (TextUtils.isEmpty(email)) {
            emailLoginLayout.setError(context.getString(R.string.login_email_error));
            validate = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordLoginLayout.setError(context.getString(R.string.login_password_error));
            validate = false;
        }

        return validate;
    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Błędny email lub hasło", Toast.LENGTH_LONG).show();
                        Log.d(LoginService.class.getName(), String.valueOf(task.getException()));
                    } else {
                        context.openMainActivity();
                    }
                });
    }

    private void clearErrorMessage() {
        emailLoginLayout.setError(null);
        passwordLoginLayout.setError(null);
    }
}
