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
import org.json.JSONObject;

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
                boolean isPromoted = checkPromoted.isChecked();

                if (name.isEmpty() || location.isEmpty() || dateTime.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this,
                            getString(R.string.fill_required_fields),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int capacity = 0;
                if (isPromoted) {
                    String capacityText = etCapacity.getText().toString().trim();

                    if (capacityText.isEmpty()) {
                        Toast.makeText(CreateEventActivity.this,
                                getString(R.string.invalid_capacity),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

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
                }

                final int finalCapacity = capacity;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", name);
                            jsonObject.put("description", description);
                            jsonObject.put("location", location);
                            jsonObject.put("eventTime", dateTime);
                            jsonObject.put("category", category);
                            jsonObject.put("promoted", isPromoted);
                            jsonObject.put("capacity", finalCapacity);


                            JSONObject response = HttpHelper.postJSONObjectToUrl(
                                    HttpHelper.BASE_URL + "/events",
                                    jsonObject
                            );

                            if (response != null) {
                                String serverId = response.getString("_id");
                                String returnedName = response.getString("name");
                                String returnedDescription = response.getString("description");
                                String returnedLocation = response.getString("location");
                                String returnedDateTime = response.getString("eventTime");
                                String returnedCategory = response.getString("category");
                                boolean returnedPromoted = response.getBoolean("promoted");
                                int returnedCapacity = response.optInt("capacity", 0);

                                Event newEvent;

                                if (returnedPromoted) {
                                    newEvent = EventFactory.createPromotedEvent(
                                            returnedName,
                                            returnedDescription,
                                            returnedLocation,
                                            returnedDateTime,
                                            returnedCategory,
                                            R.drawable.ic_launcher_foreground,
                                            returnedCapacity
                                    );
                                } else {
                                    newEvent = EventFactory.createRegularEvent(
                                            returnedName,
                                            returnedDescription,
                                            returnedLocation,
                                            returnedDateTime,
                                            returnedCategory,
                                            R.drawable.ic_launcher_foreground
                                    );
                                }

                                long result = dbHelper.insertEvent(serverId, newEvent);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (result != -1) {
                                            Toast.makeText(CreateEventActivity.this,
                                                    getString(R.string.event_created_successfully),
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(CreateEventActivity.this,
                                                    getString(R.string.event_create_failed),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CreateEventActivity.this,
                                                getString(R.string.event_create_failed),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateEventActivity.this,
                                            getString(R.string.event_create_failed),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}