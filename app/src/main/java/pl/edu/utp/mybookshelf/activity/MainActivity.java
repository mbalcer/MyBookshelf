package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.fragment.BookshelfFragment;
import pl.edu.utp.mybookshelf.activity.fragment.ExploreFragment;
import pl.edu.utp.mybookshelf.activity.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new BookshelfFragment());

        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bookshelf_page:
                    loadFragment(new BookshelfFragment());
                    break;
                case R.id.explore_page:
                    loadFragment(new ExploreFragment());
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
}