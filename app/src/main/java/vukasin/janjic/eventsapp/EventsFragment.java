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
        setActiveCategory(btnCategoryAll);

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
                setActiveCategory(btnCategoryAll);
            }
        });

        btnCategoryParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Party"));
                setActiveCategory(btnCategoryParty);
            }
        });

        btnCategoryFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Festival"));
                setActiveCategory(btnCategoryFestival);
            }
        });

        btnCategoryTheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Stand-Up & Theater"));
                setActiveCategory(btnCategoryTheater);
            }
        });

        btnCategoryConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Concert"));
                setActiveCategory(btnCategoryConcert);
            }
        });

        btnCategoryExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEvents(AppData.getSortedEventsByCategory("Exhibition"));
                setActiveCategory(btnCategoryExhibition);
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

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.setEvents(AppData.getSortedEvents());
            setActiveCategory(btnCategoryAll);
        }
    }

    private void resetCategoryButtons() {
        btnCategoryAll.setBackgroundColor(getResources().getColor(R.color.black, null));
        btnCategoryParty.setBackgroundColor(getResources().getColor(R.color.black, null));
        btnCategoryFestival.setBackgroundColor(getResources().getColor(R.color.black, null));
        btnCategoryTheater.setBackgroundColor(getResources().getColor(R.color.black, null));
        btnCategoryConcert.setBackgroundColor(getResources().getColor(R.color.black, null));
        btnCategoryExhibition.setBackgroundColor(getResources().getColor(R.color.black, null));
    }

    private void setActiveCategory(Button activeButton) {
        resetCategoryButtons();
        activeButton.setBackgroundColor(getResources().getColor(R.color.category_active, null));
    }


}