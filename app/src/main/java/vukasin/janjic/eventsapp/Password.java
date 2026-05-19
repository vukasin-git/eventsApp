package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    EditText etOldPassword, etNewPassword;
    Button btnSavePassword;
    DatabaseHelper dbHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);

        dbHelper =DatabaseHelper.getInstance(this);
        username = getIntent().getStringExtra("username");

        btnSavePassword.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString().trim();
            String newPassword= etNewPassword.getText().toString().trim();
            if(oldPassword.isEmpty() || newPassword.isEmpty()){
                Toast.makeText(Password.this,getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            boolean correctOldPassword = dbHelper.checkUser(username,oldPassword);
            if(!correctOldPassword){
                Toast.makeText(Password.this,
                        getString(R.string.wrong_old_password),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String newHashedPassword = PasswordHasher.hashPassword(newPassword);
            boolean success=dbHelper.updateUserPassword(username,newHashedPassword);
            if(success){
                Toast.makeText(Password.this,
                        getString(R.string.password_changed),
                        Toast.LENGTH_SHORT).show();
            finish();
            }else{
                Toast.makeText(Password.this,
                        getString(R.string.password_change_failed),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}