package vukasin.janjic.eventsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyEventsFragment extends Fragment {

    Button btnInterestedEvents, btnAttendingEvents, btnMyProfile;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        btnInterestedEvents = view.findViewById(R.id.btnInterestedEvents);
        btnAttendingEvents = view.findViewById(R.id.btnAttendingEvents);
        btnMyProfile = view.findViewById(R.id.btnMyProfile);

        btnInterestedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterestedEventsActivity.class);
                startActivity(intent);
            }
        });

        btnAttendingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttendingEventsActivity.class);
                startActivity(intent);
            }
        });

        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent currentIntent = getActivity().getIntent();

                String username = currentIntent.getStringExtra("username");
                String email = currentIntent.getStringExtra("email");

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        return view;
    }
}