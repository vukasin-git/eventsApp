package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ImageView imgDetailsEvent;
    TextView tvDetailsName;
    TextView tvDetailsDescription;
    TextView tvDetailsCategory;
    TextView tvDetailsLocation;
    TextView tvDetailsDateTime;
    TextView tvDetailsFreePlaces;
    TextView tvDetailsRating;

    Button btnInterested;
    Button btnAttending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        dbHelper = DatabaseHelper.getInstance(this);

        imgDetailsEvent = findViewById(R.id.imgDetailsEvent);
        tvDetailsName = findViewById(R.id.tvDetailsName);
        tvDetailsDescription = findViewById(R.id.tvDetailsDescription);
        tvDetailsCategory = findViewById(R.id.tvDetailsCategory);
        tvDetailsLocation = findViewById(R.id.tvDetailsLocation);
        tvDetailsDateTime = findViewById(R.id.tvDetailsDateTime);
        tvDetailsFreePlaces = findViewById(R.id.tvDetailsFreePlaces);
        tvDetailsRating = findViewById(R.id.tvDetailsRating);

        btnInterested = findViewById(R.id.btnInterested);
        btnAttending = findViewById(R.id.btnAttending);

        String eventName = getIntent().getStringExtra("event_name");

        //PROMENA
        Event event = dbHelper.findEventByName(eventName);

        if (event != null) {
            imgDetailsEvent.setImageResource(event.getImageResId());
            tvDetailsName.setText(event.getName());
            tvDetailsDescription.setText(event.getDescription());
            tvDetailsCategory.setText(getString(R.string.category_label, event.getCategory()));
            tvDetailsLocation.setText(getString(R.string.location_label, event.getLocation()));
            tvDetailsDateTime.setText(getString(R.string.datetime_label, event.getDateTime()));

            if (event.isPromoted()) {
                int freePlaces = event.getCapacity() - event.getAttendingCount();
                tvDetailsFreePlaces.setVisibility(TextView.VISIBLE);
                tvDetailsFreePlaces.setText(
                        getString(R.string.free_places, freePlaces, event.getCapacity())
                );
            } else {
                tvDetailsFreePlaces.setVisibility(TextView.GONE);
            }

            if (event.getRatingCount() > 0) {
                tvDetailsRating.setText(
                        getString(R.string.average_rating,
                                event.getAverageRating(),
                                event.getRatingCount())
                );
            } else {
                tvDetailsRating.setText(getString(R.string.no_ratings));
            }
        }

        btnInterested.setOnClickListener(v -> {
            Toast.makeText(EventDetailsActivity.this,
                    getString(R.string.added_to_interested),
                    Toast.LENGTH_SHORT).show();
        });

        btnAttending.setOnClickListener(v -> {
            Toast.makeText(EventDetailsActivity.this,
                    getString(R.string.signed_up_attending),
                    Toast.LENGTH_SHORT).show();
        });
    }
}