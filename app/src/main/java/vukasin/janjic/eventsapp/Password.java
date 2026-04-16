package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    EditText etOldPassword, etNewPassword;
    Button btnSavePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);

        btnSavePassword.setOnClickListener(v -> {
            Toast.makeText(Password.this,
                    getString(R.string.password_changed),
                    Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}