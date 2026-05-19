package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileUsername, tvProfileEmail;
    Button btnPassword, btnEndSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        btnPassword = findViewById(R.id.btnPassword);
        btnEndSession = findViewById(R.id.btnEndSession);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            String email = bundle.getString("email");

            tvProfileUsername.setText( username);
            tvProfileEmail.setText(email);
        }

        btnPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, Password.class);

            if(bundle != null){
                String username = bundle.getString("username");
                intent.putExtra("username",username);
            }
            startActivity(intent);
        });

        btnEndSession.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}