package vukasin.janjic.eventsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendActivityAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FriendActivityItem> mItems;

    public FriendActivityAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<FriendActivityItem>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(ArrayList<FriendActivityItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_activity_list_item, null);
        }

        TextView tvFriendActivity = convertView.findViewById(R.id.tvFriendActivity);
        FriendActivityItem item = (FriendActivityItem) getItem(position);
        tvFriendActivity.setText(item.getDisplayText());

        return convertView;
    }
}