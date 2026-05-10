package vukasin.janjic.eventsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Event> mEvents;

    public EventAdapter(Context context) {
        mContext = context;
        mEvents = new ArrayList<Event>();
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
        ImageView imgEvent;
        TextView tvEventName;
        TextView tvEventCategory;
        TextView tvEventLocation;
        TextView tvEventDateTime;
        TextView tvFeatured;
        TextView tvFreePlaces;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.event_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imgEvent = convertView.findViewById(R.id.imgEvent);
            viewHolder.tvEventName = convertView.findViewById(R.id.tvEventName);
            viewHolder.tvEventCategory = convertView.findViewById(R.id.tvEventCategory);
            viewHolder.tvEventLocation = convertView.findViewById(R.id.tvEventLocation);
            viewHolder.tvEventDateTime=convertView.findViewById(R.id.tvEventDateTime);
            viewHolder.tvFeatured = convertView.findViewById(R.id.tvFeatured);
            viewHolder.tvFreePlaces = convertView.findViewById(R.id.tvFreePlaces);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);

        viewHolder.imgEvent.setImageResource(event.getImageResId());
        viewHolder.tvEventName.setText(event.getName());
        viewHolder.tvEventCategory.setText(event.getCategory());
        viewHolder.tvEventLocation.setText( event.getLocation());
        viewHolder.tvEventDateTime.setText(event.getDateTime());

        if (event.isPromoted()) {
            viewHolder.tvFeatured.setVisibility(View.VISIBLE);
            viewHolder.tvFreePlaces.setVisibility(View.VISIBLE);

            int freePlaces = event.getCapacity() - event.getAttendingCount();
            viewHolder.tvFreePlaces.setText(
                    mContext.getString(R.string.free_places, freePlaces, event.getCapacity())
            );

            convertView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.promoted_event_background,mContext.getTheme())
            );
        } else {
            viewHolder.tvFeatured.setVisibility(View.GONE);
            viewHolder.tvFreePlaces.setVisibility(View.GONE);

            convertView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.white,mContext.getTheme())
            );
        }

        return convertView;
    }
}