package pl.edu.utp.mybookshelf.activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.service.LoginService;
import pl.edu.utp.mybookshelf.service.RegisterService;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rootView, loginLayout, registerLayout;
    private ImageView bookIconImageView;
    private RegisterService registerService;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rootView = findViewById(R.id.loginRootView);
        bookIconImageView = findViewById(R.id.bookIconImageView);

        initSwitchBetweenLoginAndRegister();
        initLoadingApplicationScreen().start();

        registerService = new RegisterService(this, registerLayout);
        loginService = new LoginService(this, loginLayout);
    }

    private void initSwitchBetweenLoginAndRegister() {
        loginLayout = findViewById(R.id.loginLayout);
        loginLayout.findViewById(R.id.loginSignUpButton).setOnClickListener(view -> {
            bookIconImageView.animate()
                    .x(Resources.getSystem().getDisplayMetrics().widthPixels - 278).y(100)
                    .setDuration(500)
                    .setListener(loginRegisterAnimationListener(true));
        });
        registerLayout = findViewById(R.id.registerLayout);
        registerLayout.findViewById(R.id.signUpLoginButton).setOnClickListener(view -> {
            bookIconImageView.animate()
                    .x(50).y(100)
                    .setDuration(500)
                    .setListener(loginRegisterAnimationListener(false));
        });
    }

    private CountDownTimer initLoadingApplicationScreen() {
        return new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                TextView bookITextView = findViewById(R.id.bookIconTextView);
                bookITextView.setVisibility(View.GONE);
                CircularProgressIndicator progressBar = findViewById(R.id.loadingProgressBar);
                progressBar.setVisibility(View.GONE);

                rootView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.clouds));
                bookIconImageView.setImageResource(R.drawable.books);
                bookIconImageView.animate()
                        .x(50).y(100)
                        .setDuration(1000)
                        .setListener(loginRegisterAnimationListener(false));
            }
        };
    }

    private Animator.AnimatorListener loginRegisterAnimationListener(boolean openRegister) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (openRegister) loginLayout.setVisibility(View.GONE);
                else registerLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (openRegister) registerLayout.setVisibility(View.VISIBLE);
                else loginLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        };
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
