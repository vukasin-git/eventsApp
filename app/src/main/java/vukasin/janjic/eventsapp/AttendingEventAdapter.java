package vukasin.janjic.eventsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendingEventAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Event> mEvents;
    private boolean showRateButton;

    public AttendingEventAdapter(Context context, boolean showRateButton) {
        mContext = context;
        mEvents = new ArrayList<Event>();
        this.showRateButton = showRateButton;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    public void clearEvents() {
        mEvents.clear();
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvAttendingEventName;
        TextView tvAttendingEventDateTime;
        TextView tvAttendingEventLocation;
        Button btnRateEvent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.attending_event_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tvAttendingEventName = convertView.findViewById(R.id.tvAttendingEventName);
            viewHolder.tvAttendingEventDateTime = convertView.findViewById(R.id.tvAttendingEventDateTime);
            viewHolder.tvAttendingEventLocation = convertView.findViewById(R.id.tvAttendingEventLocation);
            viewHolder.btnRateEvent = convertView.findViewById(R.id.btnRateEvent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);

        viewHolder.tvAttendingEventName.setText(event.getName());
        viewHolder.tvAttendingEventDateTime.setText(event.getDateTime());
        viewHolder.tvAttendingEventLocation.setText(event.getLocation());

        if (showRateButton) {
            viewHolder.btnRateEvent.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnRateEvent.setVisibility(View.GONE);
        }

        return convertView;
    }
}