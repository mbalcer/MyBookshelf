package pl.edu.utp.mybookshelf.activity;

import android.animation.Animator;
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

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rootView;
    private RelativeLayout loginRegisterView;
    private ImageView bookIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rootView = findViewById(R.id.loginRootView);
        bookIconImageView = findViewById(R.id.bookIconImageView);
        loginRegisterView = findViewById(R.id.afterAnimationView);

        initCountDownTimer().start();
    }

    private CountDownTimer initCountDownTimer() {
        return new CountDownTimer(3000, 1000) {
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
                bookIconImageView.animate().x(50).y(100).setDuration(1000).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) { }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        loginRegisterView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
            }
        };
    }
}
