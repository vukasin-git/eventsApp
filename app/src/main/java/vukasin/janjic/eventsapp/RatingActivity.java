package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    TextView tvRatingTitle, tvRatingEventName;
    Button btnStar1, btnStar2, btnStar3, btnStar4, btnStar5, btnConfirmRating;

    int selectedRating = 0;
    Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        tvRatingTitle = findViewById(R.id.tvRatingTitle);
        tvRatingEventName = findViewById(R.id.tvRatingEventName);

        btnStar1 = findViewById(R.id.btnStar1);
        btnStar2 = findViewById(R.id.btnStar2);
        btnStar3 = findViewById(R.id.btnStar3);
        btnStar4 = findViewById(R.id.btnStar4);
        btnStar5 = findViewById(R.id.btnStar5);
        btnConfirmRating = findViewById(R.id.btnConfirmRating);

        String eventName = getIntent().getStringExtra("event_name");
        tvRatingEventName.setText(eventName);

        currentEvent = AppData.findByName(eventName);

        btnStar1.setOnClickListener(v -> setSelectedRating(1));
        btnStar2.setOnClickListener(v -> setSelectedRating(2));
        btnStar3.setOnClickListener(v -> setSelectedRating(3));
        btnStar4.setOnClickListener(v -> setSelectedRating(4));
        btnStar5.setOnClickListener(v -> setSelectedRating(5));

        btnConfirmRating.setOnClickListener(v -> {
            if (selectedRating == 0) {
                Toast.makeText(RatingActivity.this,
                        getString(R.string.choose_rating_first),
                        Toast.LENGTH_SHORT).show();
            } else {
                if (currentEvent != null) {
                    currentEvent.addRating(selectedRating);
                }

                Toast.makeText(RatingActivity.this,
                        getString(R.string.rating_saved),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        updateStarColors();
    }

    private void setSelectedRating(int rating) {
        selectedRating = rating;
        updateStarColors();
    }

    private void updateStarColors() {
        int activeColor = getResources().getColor(R.color.star_active);
        int inactiveColor = getResources().getColor(R.color.star_inactive);

        btnStar1.setTextColor(selectedRating >= 1 ? activeColor : inactiveColor);
        btnStar2.setTextColor(selectedRating >= 2 ? activeColor : inactiveColor);
        btnStar3.setTextColor(selectedRating >= 3 ? activeColor : inactiveColor);
        btnStar4.setTextColor(selectedRating >= 4 ? activeColor : inactiveColor);
        btnStar5.setTextColor(selectedRating >= 5 ? activeColor : inactiveColor);
    }
}