package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    TextView tvWelcome,textUser;
    Button btnEvents, btnMyEvents, btnFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tvWelcome = findViewById(R.id.textWelcome);
        textUser=findViewById(R.id.textUser);
        btnEvents = findViewById(R.id.btnEvents);
        btnMyEvents = findViewById(R.id.btnMyEvents);
        btnFriends = findViewById(R.id.btnFriends);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            tvWelcome.setText(getString(R.string.welcome_user));
            textUser.setText(username);
        }

        loadFragment(new EventsFragment());

        btnEvents.setOnClickListener(v -> loadFragment(new EventsFragment()));
        btnMyEvents.setOnClickListener(v -> loadFragment(new MyEventsFragment()));
        btnFriends.setOnClickListener(v -> loadFragment(new FriendsFragment()));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.kontejnerFragmenta, fragment)
                .commit();
    }
}