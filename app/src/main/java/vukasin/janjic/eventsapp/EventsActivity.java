package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EventsActivity extends AppCompatActivity {

    TextView tvWelcome,textUser;
    Button btnEvents, btnMyEvents, btnFriends;
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }
    }

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
        requestNotificationPermission();
        Intent serviceIntent = new Intent(this, ExclusiveEventService.class);
        startService(serviceIntent);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.kontejnerFragmenta, fragment)
                .commit();
    }
}