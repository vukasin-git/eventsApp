package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity {

    TextView emptyAttendingView, tvUpcomingHeader, tvPastHeader;
    ListView listUpcomingEvents, listPastEvents;

    AttendingEventAdapter upcomingAdapter, pastAdapter;

    DatabaseHelper dbHelper;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        emptyAttendingView = findViewById(R.id.emptyAttendingView);
        tvUpcomingHeader = findViewById(R.id.tvUpcomingHeader);
        tvPastHeader = findViewById(R.id.tvPastHeader);

        listUpcomingEvents = findViewById(R.id.listUpcomingEvents);
        listPastEvents = findViewById(R.id.listPastEvents);

        dbHelper = DatabaseHelper.getInstance(this);
        currentUsername = getIntent().getStringExtra("username");

        upcomingAdapter = new AttendingEventAdapter(this, false);
        pastAdapter = new AttendingEventAdapter(this, true);

        listUpcomingEvents.setAdapter(upcomingAdapter);
        listPastEvents.setAdapter(pastAdapter);

        fetchAttendingEventsFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAttendingEventsFromServer();
    }

    private void fetchAttendingEventsFromServer() {
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
                                loadAttendingEventsFromLocal();
                            }
                        });
                        return;
                    }

                    JSONArray response = HttpHelper.getJSONArrayFromUrl(
                            HttpHelper.BASE_URL + "/attendance/" + userServerId
                    );

                    ArrayList<Event> attendingEvents = new ArrayList<Event>();

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

                                if (commitment.equals("PRISUSTVUJE")) {
                                    attendingEvents.add(event);
                                }
                            }
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadAttendingEvents(attendingEvents);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadAttendingEventsFromLocal();
                        }
                    });
                }
            }
        }).start();
    }

    private void loadAttendingEventsFromLocal() {
        ArrayList<Event> attendingEvents = dbHelper.readAttendingEventsForUser(currentUsername);
        loadAttendingEvents(attendingEvents);
    }

    private void loadAttendingEvents(ArrayList<Event> attendingEvents) {
        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        ArrayList<Event> pastEvents = new ArrayList<Event>();

        for (Event event : attendingEvents) {
            if (event.isPast()) {
                pastEvents.add(event);
            } else {
                upcomingEvents.add(event);
            }
        }

        if (attendingEvents.isEmpty()) {
            emptyAttendingView.setVisibility(View.VISIBLE);

            tvUpcomingHeader.setVisibility(View.GONE);
            listUpcomingEvents.setVisibility(View.GONE);

            tvPastHeader.setVisibility(View.GONE);
            listPastEvents.setVisibility(View.GONE);
        } else {
            emptyAttendingView.setVisibility(View.GONE);

            if (upcomingEvents.isEmpty()) {
                tvUpcomingHeader.setVisibility(View.GONE);
                listUpcomingEvents.setVisibility(View.GONE);
            } else {
                tvUpcomingHeader.setVisibility(View.VISIBLE);
                listUpcomingEvents.setVisibility(View.VISIBLE);
                upcomingAdapter.setEvents(upcomingEvents);
            }

            if (pastEvents.isEmpty()) {
                tvPastHeader.setVisibility(View.GONE);
                listPastEvents.setVisibility(View.GONE);
            } else {
                tvPastHeader.setVisibility(View.VISIBLE);
                listPastEvents.setVisibility(View.VISIBLE);
                pastAdapter.setEvents(pastEvents);
            }
        }
    }
}