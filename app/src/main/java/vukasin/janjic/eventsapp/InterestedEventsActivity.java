package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InterestedEventsActivity extends AppCompatActivity {

    ListView listInterestedEvents;
    TextView emptyInterestedView;
    EventAdapter adapter;

    DatabaseHelper dbHelper;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        listInterestedEvents = findViewById(R.id.listInterestedEvents);
        emptyInterestedView = findViewById(R.id.emptyInterestedView);

        dbHelper = DatabaseHelper.getInstance(this);
        currentUsername=getIntent().getStringExtra("username");

        adapter = new EventAdapter(this);
        listInterestedEvents.setAdapter(adapter);
        listInterestedEvents.setEmptyView(emptyInterestedView);
        if(currentUsername != null){
            ArrayList<Event> interestedEvents = dbHelper.readInterestedEventsForUser(currentUsername);
            adapter.setEvents(interestedEvents);
        }


        listInterestedEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Event event = (Event) adapter.getItem(position);

                Intent intent = new Intent(InterestedEventsActivity.this, EventDetailsActivity.class);
                intent.putExtra("event_name", event.getName());
                intent.putExtra("username",currentUsername);
                startActivity(intent);
            }
        });
    }
}
