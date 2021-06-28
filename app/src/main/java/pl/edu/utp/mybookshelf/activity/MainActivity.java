package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
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

    private ActionBar toolbar;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();

        dbHelper = new DBHelper(this);
        initLocalDatabase();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("tab") != null) {
            int tab = getIntent().getExtras().getInt("tab");
            getIntent().removeExtra("tab");
            switch (tab) {
                case 1:
                    loadFragment(new SearchFragment());
                    bottomNavigation.setSelectedItemId(R.id.explore_page);
                    break;
                case 2:
                    loadFragment(new SettingsFragment());
                    bottomNavigation.setSelectedItemId(R.id.settings_page);
                    break;
                default:
                    loadFragment(new BookshelfFragment());
            }
        } else {
            loadFragment(new BookshelfFragment());
        }

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bookshelf_page:
                    loadFragment(new BookshelfFragment());
                    toolbar.setTitle(R.string.bookshelf);
                    break;
                case R.id.explore_page:
                    loadFragment(new SearchFragment());
                    toolbar.setTitle(R.string.search);
                    break;
                case R.id.settings_page:
                    loadFragment(new SettingsFragment());
                    toolbar.setTitle(R.string.settings);
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
        dbHelper.setBookAsToRead("1");
        dbHelper.setBookAsToRead("2");
        dbHelper.setBookAsToRead("3");
        dbHelper.setBookAsToRead("4");
        dbHelper.setBookAsToRead("5");
        dbHelper.setBookAsRead("1");
        dbHelper.setBookAsRead("2");
        dbHelper.setBookAsRead("3");
    }

}