package vukasin.janjic.eventsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class EventsFragment extends Fragment {

    ListView listEvents;
    EventAdapter adapter;

    Button btnCategoryAll;
    Button btnCategoryParty;
    Button btnCategoryFestival;
    Button btnCategoryTheater;
    Button btnCategoryConcert;
    Button btnCategoryExhibition;
    Button btnAddEvent;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        listEvents = view.findViewById(R.id.listEvents);

        btnCategoryAll = view.findViewById(R.id.btnCategoryAll);
        btnCategoryParty = view.findViewById(R.id.btnCategoryParty);
        btnCategoryFestival = view.findViewById(R.id.btnCategoryFestival);
        btnCategoryTheater = view.findViewById(R.id.btnCategoryTheater);
        btnCategoryConcert = view.findViewById(R.id.btnCategoryConcert);
        btnCategoryExhibition = view.findViewById(R.id.btnCategoryExhibition);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);

        adapter = new EventAdapter(getActivity());
        listEvents.setAdapter(adapter);

        adapter.setEvents(AppData.getSortedEvents());

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event event = (Event) adapter.getItem(position);

                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("event_name", event.getName());
                startActivity(intent);
            }
        });

        btnCategoryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEvents());
            }
        });

        btnCategoryParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Party"));
            }
        });

        btnCategoryFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Festival"));
            }
        });

        btnCategoryTheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Stand-Up & Theater"));
            }
        });

        btnCategoryConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Concert"));
            }
        });

        btnCategoryExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Exhibition"));
            }
        });
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    //on resume za reload nakon unosa novog dogadjaja
    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.setEvents(AppData.getSortedEvents());
        }
    }
}