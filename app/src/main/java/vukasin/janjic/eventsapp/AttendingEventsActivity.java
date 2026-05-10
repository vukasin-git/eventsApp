package vukasin.janjic.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity {

    TextView emptyAttendingView, tvUpcomingHeader, tvPastHeader;
    ListView listUpcomingEvents, listPastEvents;

    AttendingEventAdapter upcomingAdapter, pastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        emptyAttendingView = findViewById(R.id.emptyAttendingView);
        tvUpcomingHeader = findViewById(R.id.tvUpcomingHeader);
        tvPastHeader = findViewById(R.id.tvPastHeader);

        listUpcomingEvents = findViewById(R.id.listUpcomingEvents);
        listPastEvents = findViewById(R.id.listPastEvents);

        upcomingAdapter = new AttendingEventAdapter(this, false);
        pastAdapter = new AttendingEventAdapter(this, true);

        listUpcomingEvents.setAdapter(upcomingAdapter);
        listPastEvents.setAdapter(pastAdapter);

        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        ArrayList<Event> pastEvents = new ArrayList<Event>();

        for (Event event : AppData.attendingEvents) {
            if (event.isPast()) {
                pastEvents.add(event);
            } else {
                upcomingEvents.add(event);
            }
        }

        if (AppData.attendingEvents.isEmpty()) {
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