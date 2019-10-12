package si.example.mybeenapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import si.example.mybeenapp.model.User;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            UserFragment fragment = new UserFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, UserFragment.TAG)
                    .commit();
        }
    }

    public void navigateToAlbum(User user) {
        AlbumFragment albumFragment = AlbumFragment.newInstance(user.id);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(AlbumFragment.TAG)
                .replace(R.id.fragment_container, albumFragment, AlbumFragment.TAG)
                .commit();
    }
}
