package pl.edu.utp.mybookshelf.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.edu.utp.mybookshelf.activity.fragment.BookInfoFragment;
import pl.edu.utp.mybookshelf.activity.fragment.BookMyDataFragment;
import pl.edu.utp.mybookshelf.activity.fragment.BookReviewsFragment;

public class BookViewPagerAdapter extends FragmentStateAdapter {

    public BookViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new BookInfoFragment();
            case 2: return new BookReviewsFragment();
            default: return new BookMyDataFragment(); // 0 or other
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
