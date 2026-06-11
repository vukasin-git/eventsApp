package vukasin.janjic.eventsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import org.json.JSONObject;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
public class ExclusiveEventService extends Service {
    private static final String LOG_TAG = "ExclusiveEventService;";
    private static final String CHANNEL_ID = "exclusive_event_channel";
    private static final int EXCLUSIVE_NOTIFICATION_ID = 1001;

    private void createNotificationChannel() {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.exclusive_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getString(R.string.exclusive_channel_description));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }
    private void showExclusiveEventNotification(String eventName, String eventServerId, long remainingMillis) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("event_server_id", eventServerId);
        String currentUsername = getSharedPreferences("events_app_prefs",MODE_PRIVATE)
                .getString("logged_in_username",null);
        if(currentUsername!=null){
            intent.putExtra("username",currentUsername);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        String contentText = eventName+ " " +getString(R.string.exclusive_notification_text)+ " "+
                formatRemainingTime(remainingMillis);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getString(R.string.exclusive_notification_title))
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(EXCLUSIVE_NOTIFICATION_ID, builder.build());
    }
    private void startNotificationCountdown(String eventName, String eventServerId, long deadlineMillis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long remainingMillis = deadlineMillis - System.currentTimeMillis();

                    if (remainingMillis <= 0) {
                        showExclusiveWindowClosedNotification();
                        break;
                    }

                    showExclusiveEventNotification(eventName, eventServerId, remainingMillis);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    private long saveExclusiveEventWindow(String eventServerId) {
        long deadlineMillis = System.currentTimeMillis() + 1 * 30 * 1000;

        getSharedPreferences("events_app_prefs", MODE_PRIVATE)
                .edit()
                .putLong("exclusive_deadline_"+eventServerId, deadlineMillis)
                .apply();
        return deadlineMillis;
    }

    private String generateExclusiveEventName() {
        String timePart = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .format(new Date());

        return "Exclusive Event " + timePart;
    }
    private void showExclusiveWindowClosedNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.exclusive_window_closed_title))
                .setContentText(getString(R.string.exclusive_window_closed_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(EXCLUSIVE_NOTIFICATION_ID + 1, builder.build());
    }

    private String formatRemainingTime(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
    DatabaseHelper dbHelper;
    public ExclusiveEventService() {

    }
    public void onCreate(){
        super.onCreate();
        Log.d(LOG_TAG,"onCreate");
        dbHelper=DatabaseHelper.getInstance(this);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    String generatedEventName = generateExclusiveEventName();
                    jsonObject.put("name", generatedEventName);
                    jsonObject.put("description", "Special limited-time event.111");
                    jsonObject.put("location", "Novi Sad");
                    jsonObject.put("eventTime", "15.06.2026 20:00");
                    jsonObject.put("category", "Exclusive");
                    jsonObject.put("promoted", true);
                    jsonObject.put("capacity", 100);

                    JSONObject response = HttpHelper.postJSONObjectToUrl(
                            HttpHelper.BASE_URL + "/events",
                            jsonObject
                    );

                    if (response != null) {
                        String serverId = response.getString("_id");
                        String name = response.getString("name");
                        String description = response.optString("description", "");
                        String location = response.getString("location");
                        String eventTime = response.getString("eventTime");
                        String category = response.getString("category");
                        boolean promoted = response.getBoolean("promoted");
                        int capacity = response.optInt("capacity", 0);
                        int attendingCount = response.optInt("numberOfAttendees", 0);
                        double averageRating = response.optDouble("avgRating", 0.0);
                        int ratingCount = response.optInt("numberOfRatings", 0);

                        Event event;

                        if (promoted) {
                            event = EventFactory.createPromotedEvent(
                                    name,
                                    description,
                                    location,
                                    eventTime,
                                    category,
                                    R.drawable.promo,
                                    capacity,
                                    attendingCount,
                                    averageRating,
                                    ratingCount
                            );
                        } else {
                            event = EventFactory.createRegularEvent(
                                    name,
                                    description,
                                    location,
                                    eventTime,
                                    category,
                                    R.drawable.ic_launcher_foreground,
                                    attendingCount,
                                    averageRating,
                                    ratingCount
                            );
                        }

                        long result = dbHelper.insertOrUpdateEventFromServer(serverId, event);

                        Log.d(LOG_TAG, "Exclusive event created. DB result = " + result);

                        long deadlineMillis = saveExclusiveEventWindow(serverId);
                        startNotificationCountdown(name, serverId,deadlineMillis);
                    } else {
                        Log.d(LOG_TAG, "Response is null");
                    }

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to create exclusive event", e);
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}