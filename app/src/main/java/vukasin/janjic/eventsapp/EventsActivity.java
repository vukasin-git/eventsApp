package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    TextView tvWelcome, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvEmail = findViewById(R.id.tvEmail);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String username = bundle.getString("username");
            String email = bundle.getString("email");

            tvWelcome.setText("Username: " + username);
            tvEmail.setText("Email: " + email);
        }
    }
}