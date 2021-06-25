package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import pl.edu.utp.mybookshelf.R;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
