package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.fragment.BookshelfFragment;
import pl.edu.utp.mybookshelf.activity.fragment.SearchFragment;
import pl.edu.utp.mybookshelf.activity.fragment.SettingsFragment;
import pl.edu.utp.mybookshelf.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        initLocalDatabase();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("resultScanner") != null) {
            loadFragment(new SearchFragment());
            bottomNavigation.setSelectedItemId(R.id.explore_page);
        } else {
            loadFragment(new BookshelfFragment());
        }

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bookshelf_page:
                    loadFragment(new BookshelfFragment());
                    break;
                case R.id.explore_page:
                    loadFragment(new SearchFragment());
                    break;
                case R.id.settings_page:
                    loadFragment(new SettingsFragment());
                    break;
                default:
                    Log.e(MainActivity.class.getName(), "Item id was not found");
                    break;
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initLocalDatabase() {
        dbHelper.setBookAsToRead(1L);
        dbHelper.setBookAsToRead(2L);
        dbHelper.setBookAsToRead(3L);
        dbHelper.setBookAsToRead(4L);
        dbHelper.setBookAsToRead(5L);
        dbHelper.setBookAsRead(1L);
        dbHelper.setBookAsRead(2L);
        dbHelper.setBookAsRead(3L);
    }

}