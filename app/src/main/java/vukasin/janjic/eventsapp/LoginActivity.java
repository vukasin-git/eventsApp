package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    LinearLayout pocetniEkran, loginEkran, registerEkran;

    Button loginDugme, registerDugme, loginConfirm, registerConfirm;

    EditText loginUsername, loginPassword;
    EditText registerUsername, registerPassword, registerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pocetniEkran = findViewById(R.id.pocetniEkran);
        loginEkran = findViewById(R.id.loginEkran);
        registerEkran = findViewById(R.id.registerEkran);

        loginDugme = findViewById(R.id.loginDugme);
        registerDugme = findViewById(R.id.registerDugme);
        loginConfirm = findViewById(R.id.loginConfirm);
        registerConfirm = findViewById(R.id.registerConfirm);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        registerEmail = findViewById(R.id.registerEmail);

        loginDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pocetniEkran.setVisibility(View.GONE);
                loginEkran.setVisibility(View.VISIBLE);
                registerEkran.setVisibility(View.GONE);
            }
        });

        registerDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pocetniEkran.setVisibility(View.GONE);
                loginEkran.setVisibility(View.GONE);
                registerEkran.setVisibility(View.VISIBLE);
            }
        });

        loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (username.equals("admin") && password.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, EventsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("email", getString(R.string.admin_email));

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.wrong_login), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerUsername.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(LoginActivity.this,  getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, EventsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("email", email);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}