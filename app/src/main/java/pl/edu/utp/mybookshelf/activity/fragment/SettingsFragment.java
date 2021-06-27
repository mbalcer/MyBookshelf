package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.AuthActivity;
import pl.edu.utp.mybookshelf.model.Setting;

public class SettingsFragment extends Fragment {
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_settings, container, false);
        ListView settingsListView = inflate.findViewById(R.id.settings_list);
        List<Setting> settings = Arrays.asList(Setting.values());
        ListAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, settings);

        settingsListView.setAdapter(adapter);
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(SettingsFragment.class.getName(), settings.get(position).getName());

                if (settings.get(position).equals(Setting.SIGN_OUT)) {
                    signOut();
                }
            }
        });
        return inflate;
    }

    private void signOut() {
        auth.signOut();
        Bundle extras = new Bundle();
        extras.putInt("loadingTime", 500);
        changeActivity(AuthActivity.class, extras);
    }

    private void changeActivity(Class<?> activity, Bundle extras) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtras(extras);
        startActivity(intent);
        getActivity().finish();
    }
}