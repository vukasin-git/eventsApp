package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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
        currentUsername = getIntent().getStringExtra("username");

        adapter = new EventAdapter(this);
        listInterestedEvents.setAdapter(adapter);
        listInterestedEvents.setEmptyView(emptyInterestedView);

        fetchInterestedEventsFromServer();

        listInterestedEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Event event = (Event) adapter.getItem(position);

                Intent intent = new Intent(InterestedEventsActivity.this, EventDetailsActivity.class);
                intent.putExtra("event_name", event.getName());
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchInterestedEventsFromServer();
    }

    private void fetchInterestedEventsFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userServerId = dbHelper.getUserServerIdByUsername(currentUsername);
                    int localUserId = dbHelper.getUserIdByUsername(currentUsername);

                    if (userServerId == null || localUserId == -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setEvents(dbHelper.readInterestedEventsForUser(currentUsername));
                            }
                        });
                        return;
                    }

                    JSONArray response = HttpHelper.getJSONArrayFromUrl(
                            HttpHelper.BASE_URL + "/attendance/" + userServerId
                    );

                    ArrayList<Event> interestedEvents = new ArrayList<Event>();

                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject attendanceObject = response.getJSONObject(i);

                            String commitment = attendanceObject.getString("commitment");
                            String serverEventId = attendanceObject.getString("eventId");

                            Event event = dbHelper.findEventByServerId(serverEventId);

                            if (event != null) {
                                int localEventId = dbHelper.getLocalEventIdByServerId(serverEventId);

                                if (localEventId != -1) {
                                    dbHelper.insertOrUpdateAttendance(localUserId, localEventId, commitment);
                                }

                                if (commitment.equals("ZAINTERESOVAN")) {
                                    interestedEvents.add(event);
                                }
                            }
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setEvents(interestedEvents);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setEvents(dbHelper.readInterestedEventsForUser(currentUsername));
                        }
                    });
                }
            }
        }).start();
    }
}



