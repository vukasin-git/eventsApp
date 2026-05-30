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
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import android.net.Uri;

public class EventsFragment extends Fragment {

    ListView listEvents;
    EventAdapter adapter;
    DatabaseHelper dbHelper;
    boolean isAdmin = false;

    Button btnCategoryAll;
    Button btnCategoryParty;
    Button btnCategoryFestival;
    Button btnCategoryTheater;
    Button btnCategoryConcert;
    Button btnCategoryExhibition;
    Button btnAddEvent;

    public EventsFragment() {
    }
    private void fetchAllEventsFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray response = HttpHelper.getJSONArrayFromUrl(
                            HttpHelper.BASE_URL + "/events"
                    );
                    if(response !=null){
                        dbHelper.deleteAllEvents();
                        for (int i=0;i<response.length();i++){
                            JSONObject eventObject = response.getJSONObject(i);

                            String serverId = eventObject.getString("_id");
                            String name = eventObject.getString("name");
                            String description = eventObject.getString("description");
                            String location = eventObject.getString("location");
                            String eventTime = eventObject.getString("eventTime");
                            String category = eventObject.getString("category");
                            boolean promoted = eventObject.getBoolean("promoted");
                            int capacity = eventObject.optInt("capacity", 0);
                            Event event;

                            if(promoted){
                                event = EventFactory.createPromotedEvent(
                                        name,
                                        description,
                                        location,
                                        eventTime,
                                        category,
                                        R.drawable.promo,
                                        capacity
                                );
                            }else{
                                event = EventFactory.createRegularEvent(
                                        name,
                                        description,
                                        location,
                                        eventTime,
                                        category,
                                        R.drawable.ic_launcher_foreground
                                );
                            }
                            dbHelper.insertOrUpdateEventFromServer(serverId,event);
                        }
                        if(getActivity()!= null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setEvents(dbHelper.readAllEvents());
                                    setActiveCategory(btnCategoryAll);
                                }
                            });
                        }
                    }
                }catch(Exception e){
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setEvents(dbHelper.readAllEvents());
                            }
                        });
                    }
                }
            }
        }).start();
    }
    private void fetchEventsByCategoryFromServer(String category){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String encodedCategory = Uri.encode(category);
                    JSONArray response = HttpHelper.getJSONArrayFromUrl(
                            HttpHelper.BASE_URL + "/events/" + encodedCategory
                    );
                    ArrayList<Event> categoryEvents = new ArrayList<Event>();

                    if(response != null){
                        for (int i = 0; i< response.length();i++) {
                            JSONObject eventObject = response.getJSONObject(i);
                            String serverId = eventObject.getString("_id");
                            String name = eventObject.getString("name");
                            String description = eventObject.optString("description", "");
                            String location = eventObject.getString("location");
                            String eventTime = eventObject.getString("eventTime");
                            String returnedCategory = eventObject.getString("category");
                            boolean promoted = eventObject.getBoolean("promoted");
                            int capacity = eventObject.optInt("capacity", 0);
                            Event event;
                            if (promoted) {
                                event = EventFactory.createPromotedEvent(
                                        name,
                                        description,
                                        location,
                                        eventTime,
                                        returnedCategory,
                                        R.drawable.ic_launcher_foreground,
                                        capacity
                                );
                            } else {
                                event = EventFactory.createRegularEvent(
                                        name,
                                        description,
                                        location,
                                        eventTime,
                                        returnedCategory,
                                        R.drawable.ic_launcher_foreground
                                );
                            }
                            dbHelper.insertOrUpdateEventFromServer(serverId, event);
                            categoryEvents.add(event);
                        }
                    }
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setEvents(categoryEvents);
                            }
                        });
                    }
                }catch(Exception e){
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setEvents(dbHelper.readEventsByCategory(category));
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        dbHelper=DatabaseHelper.getInstance(getActivity());
        isAdmin=getActivity().getIntent().getBooleanExtra("isAdmin",false);


        listEvents = view.findViewById(R.id.listEvents);

        btnCategoryAll = view.findViewById(R.id.btnCategoryAll);
        btnCategoryParty = view.findViewById(R.id.btnCategoryParty);
        btnCategoryFestival = view.findViewById(R.id.btnCategoryFestival);
        btnCategoryTheater = view.findViewById(R.id.btnCategoryTheater);
        btnCategoryConcert = view.findViewById(R.id.btnCategoryConcert);
        btnCategoryExhibition = view.findViewById(R.id.btnCategoryExhibition);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);

        if (isAdmin){
            btnAddEvent.setVisibility(View.VISIBLE);
        }else{
            btnAddEvent.setVisibility(View.GONE);
        }
        adapter = new EventAdapter(getActivity());
        listEvents.setAdapter(adapter);

        fetchAllEventsFromServer();

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event event = (Event) adapter.getItem(position);

                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("event_name", event.getName());
                String username = getActivity().getIntent().getStringExtra("username");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        btnCategoryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAllEventsFromServer();

            }
        });

        btnCategoryParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEventsByCategoryFromServer("Party");
                setActiveCategory(btnCategoryParty);
            }
        });

        btnCategoryFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEventsByCategoryFromServer("Festival");
                setActiveCategory(btnCategoryFestival);
            }
        });

        btnCategoryTheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEventsByCategoryFromServer("Stand-Up & Theater");
                setActiveCategory(btnCategoryTheater);
            }
        });

        btnCategoryConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEventsByCategoryFromServer("Concert");
                setActiveCategory(btnCategoryConcert);
            }
        });

        btnCategoryExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEventsByCategoryFromServer("Exhibition");
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
            adapter.setEvents(dbHelper.readAllEvents());
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