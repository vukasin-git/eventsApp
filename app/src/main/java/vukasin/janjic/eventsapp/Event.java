package vukasin.janjic.eventsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {

    private String name;
    private String description;
    private String location;
    private String dateTime;
    private String category;
    private int imageResId;
    private boolean isPromoted;
    private int capacity;
    private int attendingCount;
    private double averageRating;
    private int ratingCount;

    //Promoted event
    public Event(String name, String description, String location, String dateTime,
                 String category, int imageResId, boolean isPromoted,
                 int capacity, int attendingCount, double averageRating, int ratingCount) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = isPromoted;
        this.capacity = capacity;
        this.attendingCount = attendingCount;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    // Regular event
    public Event(String name, String description, String location, String dateTime,
                 String category, int imageResId, int attendingCount,
                 double averageRating, int ratingCount) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = false;
        this.capacity = 0;
        this.attendingCount = attendingCount;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public boolean isPast() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        try {
            Date eventDate = sdf.parse(dateTime);
            Date currentDate = new Date();
            return eventDate != null && eventDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public void addRating(int rating) {
        double total = averageRating * ratingCount;
        total += rating;
        ratingCount++;
        averageRating = total / ratingCount;
    }

    //seteri

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    //geteri

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }
}
