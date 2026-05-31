package vukasin.janjic.eventsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    ListView listFriendsActivity;
    TextView emptyFriendsView;

    DatabaseHelper dbHelper;
    String currentUsername;
    FriendActivityAdapter adapter;

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listFriendsActivity = view.findViewById(R.id.listFriendsActivity);
        emptyFriendsView = view.findViewById(R.id.emptyFriendsView);

        dbHelper = DatabaseHelper.getInstance(getActivity());
        currentUsername = getActivity().getIntent().getStringExtra("username");

        adapter = new FriendActivityAdapter(getActivity());
        listFriendsActivity.setAdapter(adapter);
        listFriendsActivity.setEmptyView(emptyFriendsView);

        fetchFriendsActivityFromServer();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFriendsActivityFromServer();
    }

    private void fetchFriendsActivityFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userServerId = dbHelper.getUserServerIdByUsername(currentUsername);

                    if (userServerId == null) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setItems(new ArrayList<FriendActivityItem>());
                                }
                            });
                        }
                        return;
                    }

                    JSONArray response = HttpHelper.getJSONArrayFromUrl(
                            HttpHelper.BASE_URL + "/friends-activity/" + userServerId
                    );

                    ArrayList<FriendActivityItem> items = new ArrayList<FriendActivityItem>();

                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);

                            String username = object.getString("username");
                            String eventName = object.getString("eventName");
                            String commitment = object.getString("commitment");

                            items.add(new FriendActivityItem(username, eventName, commitment));
                        }
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setItems(items);
                            }
                        });
                    }

                } catch (Exception e) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setItems(new ArrayList<FriendActivityItem>());
                            }
                        });
                    }
                }
            }
        }).start();
    }
}