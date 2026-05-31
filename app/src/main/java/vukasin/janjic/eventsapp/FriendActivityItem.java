package vukasin.janjic.eventsapp;

public class FriendActivityItem {

    private String username;
    private String eventName;
    private String commitment;

    public FriendActivityItem(String username, String eventName, String commitment) {
        this.username = username;
        this.eventName = eventName;
        this.commitment = commitment;
    }

    public String getDisplayText() {
        if ("PRISUSTVUJE".equals(commitment)) {
            return username + " is also attending " + eventName;
        } else {
            return username + " is also interested in " + eventName;
        }
    }
}