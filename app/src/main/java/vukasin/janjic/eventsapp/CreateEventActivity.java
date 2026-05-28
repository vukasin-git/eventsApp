package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etEventName, etEventDescription, etEventLocation, etEventDateTime, etCapacity;
    Spinner spinnerCategory;
    CheckBox checkPromoted;
    Button btnCreateEvent;

    String[] categories = {
            "Party",
            "Festival",
            "Stand-Up & Theater",
            "Concert",
            "Exhibition"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dbHelper= DatabaseHelper.getInstance(this);

        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventLocation = findViewById(R.id.etEventLocation);
        etEventDateTime = findViewById(R.id.etEventDateTime);
        etCapacity = findViewById(R.id.etCapacity);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        checkPromoted = findViewById(R.id.checkPromoted);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        checkPromoted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPromoted.isChecked()) {
                    etCapacity.setVisibility(View.VISIBLE);
                } else {
                    etCapacity.setVisibility(View.GONE);
                    etCapacity.setText("");
                }
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etEventName.getText().toString().trim();
                String description = etEventDescription.getText().toString().trim();
                String location = etEventLocation.getText().toString().trim();
                String dateTime = etEventDateTime.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                if (name.isEmpty() || location.isEmpty() || dateTime.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this,
                            getString(R.string.fill_required_fields),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Event newEvent;

                if (checkPromoted.isChecked()) {
                    String capacityText = etCapacity.getText().toString().trim();

                    if (capacityText.isEmpty()) {
                        Toast.makeText(CreateEventActivity.this,
                                getString(R.string.invalid_capacity),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int capacity;

                    try {
                        capacity = Integer.parseInt(capacityText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(CreateEventActivity.this,
                                getString(R.string.invalid_capacity),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (capacity <= 0) {
                        Toast.makeText(CreateEventActivity.this,
                                getString(R.string.invalid_capacity),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    newEvent = EventFactory.createPromotedEvent(
                            name,
                            description,
                            location,
                            dateTime,
                            category,
                            R.drawable.ic_launcher_foreground,
                            capacity
                    );
                } else {
                    newEvent = EventFactory.createRegularEvent(
                            name,
                            description,
                            location,
                            dateTime,
                            category,
                            R.drawable.ic_launcher_foreground
                    );
                }


                long result = dbHelper.insertEvent(newEvent);
                if(result!=-1){
                    Toast.makeText(CreateEventActivity.this,
                            getString(R.string.event_created_successfully),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CreateEventActivity.this,
                            getString(R.string.event_create_failed),
                            Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }
}