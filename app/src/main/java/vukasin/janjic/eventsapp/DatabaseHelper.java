package vukasin.janjic.eventsapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "EventsApp.db";
    private static final int DATABASE_VERSION = 8;

    //Users tabela
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_SERVER_ID = "serverID";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASSWORD="lozinka";
    public static final String COLUMN_USER_IS_ADMIN = "isAdmin";

    // EVENTS tabela
    public static final String TABLE_EVENTS = "Events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_SERVER_ID ="serverId";
    public static final String COLUMN_EVENT_NAME = "naziv";
    public static final String COLUMN_EVENT_DESCRIPTION = "opis";
    public static final String COLUMN_EVENT_LOCATION = "lokacija";
    public static final String COLUMN_EVENT_DATETIME = "datumVreme";
    public static final String COLUMN_EVENT_CATEGORY = "kategorija";
    public static final String COLUMN_EVENT_PROMOTED = "promoted";
    public static final String COLUMN_EVENT_CAPACITY = "kapacitet";
    public static final String COLUMN_EVENT_ATTENDING_COUNT = "brojPrisutnih";
    public static final String COLUMN_EVENT_AVG_RATING = "prosecnaOcena";
    public static final String COLUMN_EVENT_RATING_COUNT = "brojOcena";

    // ATTENDANCE tabela
    public static final String TABLE_ATTENDANCE = "Attendance";
    public static final String COLUMN_ATTENDANCE_ID = "id";
    public static final String COLUMN_ATTENDANCE_USER_ID = "userId";
    public static final String COLUMN_ATTENDANCE_EVENT_ID = "eventId";
    public static final String COLUMN_ATTENDANCE_STATUS = "prisustvo";

    // RATINGS tabela
    public static final String TABLE_RATINGS = "Ratings";
    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_USER_ID = "userId";
    public static final String COLUMN_RATING_EVENT_ID = "eventId";
    public static final String COLUMN_RATING_VALUE = "rating";


    //konstruktor
    private DatabaseHelper(Context context){
        super(context.getApplicationContext(),DATABASE_NAME,null,DATABASE_VERSION);
    }
    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance==null){
            instance =new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " +TABLE_USERS +"("+
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_SERVER_ID + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_USER_IS_ADMIN + " INTEGER DEFAULT 0 CHECK(" + COLUMN_USER_IS_ADMIN + " IN (0,1))" +
                ");";

        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_SERVER_ID + " TEXT, " +
                COLUMN_EVENT_NAME + " TEXT NOT NULL, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_LOCATION + " TEXT NOT NULL, " +
                COLUMN_EVENT_DATETIME + " TEXT NOT NULL, " +
                COLUMN_EVENT_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_EVENT_PROMOTED + " INTEGER DEFAULT 0 CHECK(" + COLUMN_EVENT_PROMOTED + " IN (0,1)), " +
                COLUMN_EVENT_CAPACITY + " INTEGER DEFAULT 0 CHECK(" + COLUMN_EVENT_CAPACITY + " >= 0), " +
                COLUMN_EVENT_ATTENDING_COUNT + " INTEGER DEFAULT 0 CHECK(" + COLUMN_EVENT_ATTENDING_COUNT + " >= 0), " +
                COLUMN_EVENT_AVG_RATING + " REAL DEFAULT 0 CHECK(" + COLUMN_EVENT_AVG_RATING + " BETWEEN 0 AND 5), " +
                COLUMN_EVENT_RATING_COUNT + " INTEGER DEFAULT 0 CHECK(" + COLUMN_EVENT_RATING_COUNT + " >= 0), " +
                "CHECK(" + COLUMN_EVENT_PROMOTED + " = 0 OR " + COLUMN_EVENT_CAPACITY + " > 0), " +
                "CHECK(" + COLUMN_EVENT_PROMOTED + " = 0 OR " + COLUMN_EVENT_ATTENDING_COUNT + " <= " + COLUMN_EVENT_CAPACITY + ")" +
                ");";

        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ATTENDANCE_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_ATTENDANCE_EVENT_ID + " INTEGER NOT NULL, " +
                COLUMN_ATTENDANCE_STATUS + " TEXT NOT NULL CHECK(" + COLUMN_ATTENDANCE_STATUS + " IN ('ZAINTERESOVAN', 'PRISUSTVUJE')), " +
                "FOREIGN KEY(" + COLUMN_ATTENDANCE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ATTENDANCE_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "), " +
                "UNIQUE(" + COLUMN_ATTENDANCE_USER_ID + ", " + COLUMN_ATTENDANCE_EVENT_ID + ")" +
                ");";

        String createRatingsTable = "CREATE TABLE " + TABLE_RATINGS + " (" +
                COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RATING_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_RATING_EVENT_ID + " INTEGER NOT NULL, " +
                COLUMN_RATING_VALUE + " INTEGER NOT NULL CHECK(" + COLUMN_RATING_VALUE + " BETWEEN 1 AND 5), " +
                "FOREIGN KEY(" + COLUMN_RATING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_RATING_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "), " +
                "UNIQUE(" + COLUMN_RATING_USER_ID + ", " + COLUMN_RATING_EVENT_ID + ")" +
                ");";
        db.execSQL(createUsersTable);
        db.execSQL(createEventsTable);
        db.execSQL(createAttendanceTable);
        db.execSQL(createRatingsTable);

        insertInitialEvents(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
    }



    //METODE USER

    public long insertUser(String serverId, String username, String email, String hashedPassword, boolean isAdmin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_SERVER_ID, serverId);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_USER_IS_ADMIN, isAdmin ? 1 : 0);

        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String username, String enteredPassword) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
            return PasswordHasher.verifyPassword(enteredPassword, storedPasswordHash);
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    public boolean updateUserPassword(String username, String newHashedPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PASSWORD, newHashedPassword);

        int rowsAffected = db.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME + " = ?",
                new String[]{username}
        );

        return rowsAffected > 0;
    }
    public boolean isUserAdmin(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_IS_ADMIN},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ADMIN));
            cursor.close();
            return isAdmin == 1;
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }
    public String getEmailByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            cursor.close();
            return email;
        }

        if (cursor != null) {
            cursor.close();
        }

        return "";
    }

    //EVENTI
    private long insertEvent(SQLiteDatabase db,String serverID, Event event){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_SERVER_ID, serverID);
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_LOCATION, event.getLocation());
        values.put(COLUMN_EVENT_DATETIME, event.getDateTime());
        values.put(COLUMN_EVENT_CATEGORY, event.getCategory());
        values.put(COLUMN_EVENT_PROMOTED, event.isPromoted() ? 1 : 0);
        values.put(COLUMN_EVENT_CAPACITY, event.getCapacity());
        values.put(COLUMN_EVENT_ATTENDING_COUNT, event.getAttendingCount());
        values.put(COLUMN_EVENT_AVG_RATING, event.getAverageRating());
        values.put(COLUMN_EVENT_RATING_COUNT, event.getRatingCount());

        return db.insert(TABLE_EVENTS,null,values);
    }
    public long insertEvent(String serverId,Event event){
        SQLiteDatabase db = getWritableDatabase();
        return insertEvent(db,serverId,event);
    }
    private void insertInitialEvents(SQLiteDatabase db) {
        Event e1 = EventFactory.createPromotedEvent(
                "EXIT Festival",
                "Najveci muzicki festival u regionu.",
                "Petrovaradin, Novi Sad",
                "15.07.2026 18:00",
                "Festival",
                R.drawable.exitfestivallogo,
                50000
        );

        Event e2 = EventFactory.createPromotedEvent(
                "NEON Party",
                "Veliki promoted party.",
                "Stark Arena, Beograd",
                "10.05.2026 22:00",
                "Party",
                R.drawable.ic_launcher_foreground,
                200
        );

        Event e3 = EventFactory.createRegularEvent(
                "Rooftop Summer Party",
                "Letnja zurka na krovu.",
                "Dorcol Platz, Beograd",
                "20.06.2026 21:00",
                "Party",
                R.drawable.ic_launcher_foreground
        );

        Event e4 = EventFactory.createRegularEvent(
                "Beer Fest",
                "Festival piva i muzike.",
                "Usce, Beograd",
                "12.08.2026 16:00",
                "Festival",
                R.drawable.ic_launcher_foreground
        );

        Event e5 = EventFactory.createRegularEvent(
                "Nikola Djuricko: Monodrama",
                "Pozorisna monodrama.",
                "Narodno pozoriste, Beograd",
                "03.05.2026 20:00",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e6 = EventFactory.createRegularEvent(
                "Hamlet",
                "Pozorisna predstava.",
                "JDP, Beograd",
                "01.03.2026 19:30",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e7 = EventFactory.createRegularEvent(
                "Filharmonija: Beethoven",
                "Koncert klasicne muzike.",
                "Kolarac, Beograd",
                "11.05.2026 19:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e8 = EventFactory.createRegularEvent(
                "Konstrakta Live",
                "Koncert uzivo.",
                "Novi Sad",
                "20.01.2026 20:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e9 = EventFactory.createRegularEvent(
                "Foto Beograd 2026",
                "Izlozba savremene fotografije.",
                "Galerija Haos, Beograd",
                "14.05.2026 11:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e10 = EventFactory.createRegularEvent(
                "Modern Art Expo",
                "Izlozba moderne umetnosti.",
                "MSU, Beograd",
                "18.06.2026 10:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e11 = EventFactory.createRegularEvent(
                "Street Musicians Festival",
                "Festival ulicnih muzicara.",
                "Knez Mihajlova, Beograd",
                "25.05.2026 12:00",
                "Festival",
                R.drawable.ic_launcher_foreground
        );

        Event e12 = EventFactory.createRegularEvent(
                "Beach Party Palic",
                "Letnja zurka na Palicu.",
                "Palic, Subotica",
                "05.08.2026 20:00",
                "Party",
                R.drawable.ic_launcher_foreground
        );

        Event e13 = EventFactory.createRegularEvent(
                "Laki Stand-Up Specijal",
                "Vece stand-up komedije.",
                "Dom omladine, Beograd",
                "15.02.2026 20:00",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e14 = EventFactory.createRegularEvent(
                "Jazz Night Nis",
                "Vece jazz muzike.",
                "Niska tvrdjava, Nis",
                "10.08.2025 19:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e15 = EventFactory.createRegularEvent(
                "Science Fair",
                "Naucna izlozba i prezentacije.",
                "Sajam, Novi Sad",
                "22.09.2026 09:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        insertEvent(db,null, e1);
        insertEvent(db, null, e2);
        insertEvent(db,null, e3);
        insertEvent(db,null, e4);
        insertEvent(db,null, e5);
        insertEvent(db,null, e6);
        insertEvent(db,null, e7);
        insertEvent(db,null, e8);
        insertEvent(db,null, e9);
        insertEvent(db,null, e10);
        insertEvent(db,null, e11);
        insertEvent(db,null, e12);
        insertEvent(db,null, e13);
        insertEvent(db,null, e14);
        insertEvent(db,null, e15);
    }
    public ArrayList<Event> readAllEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EVENT_PROMOTED + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Event event = cursorToEvent(cursor);
                events.add(event);
            }
            cursor.close();
        }

        return events;
    }

    public ArrayList<Event> readEventsByCategory(String category) {
        ArrayList<Event> events = new ArrayList<Event>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                COLUMN_EVENT_CATEGORY + " = ?",
                new String[]{category},
                null,
                null,
                COLUMN_EVENT_PROMOTED + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Event event = cursorToEvent(cursor);
                events.add(event);
            }
            cursor.close();
        }

        return events;
    }

    public Event findEventByName(String name) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                COLUMN_EVENT_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Event event = cursorToEvent(cursor);
            cursor.close();
            return event;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }



    private Event cursorToEvent(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION));
        String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION));
        String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATETIME));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_CATEGORY));
        int promoted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_PROMOTED));
        int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_CAPACITY));
        int attendingCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ATTENDING_COUNT));
        double avgRating = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_AVG_RATING));
        int ratingCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_RATING_COUNT));

        if (promoted == 1) {
            return new Event(
                    name,
                    description,
                    location,
                    dateTime,
                    category,
                    R.drawable.promo,
                    true,
                    capacity,
                    attendingCount,
                    avgRating,
                    ratingCount
            );
        } else {
            return new Event(
                    name,
                    description,
                    location,
                    dateTime,
                    category,
                    R.drawable.ic_launcher_foreground,
                    attendingCount,
                    avgRating,
                    ratingCount
            );
        }
    }

    //ATTENDANCE
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ?",
                new String[]{username.trim()},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
            return userId;
        }

        if (cursor != null) {
            cursor.close();
        }

        return -1;
    }
    public int getEventIdByName(String eventName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_ID},
                COLUMN_EVENT_NAME + "=?",
                new String[]{eventName},
                null,
                null,
                null,
                null

        );
        if(cursor != null && cursor.moveToFirst()){
            int eventId=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID));
            cursor.close();
            return eventId;
        }
        if(cursor!=null){
            cursor.close();
        }
        return -1;
    }
    public String getAttendanceStatus(int userId, int eventId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ATTENDANCE,
                new String[]{COLUMN_ATTENDANCE_STATUS},
                COLUMN_ATTENDANCE_USER_ID + " = ? AND " + COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(eventId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ATTENDANCE_STATUS));
            cursor.close();
            return status;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public long insertAttendance(int userId, int eventId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ATTENDANCE_USER_ID, userId);
        values.put(COLUMN_ATTENDANCE_EVENT_ID, eventId);
        values.put(COLUMN_ATTENDANCE_STATUS, status);

        return db.insert(TABLE_ATTENDANCE, null, values);
    }

    public boolean updateAttendanceStatus(int userId, int eventId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ATTENDANCE_STATUS, status);

        int rowsAffected = db.update(
                TABLE_ATTENDANCE,
                values,
                COLUMN_ATTENDANCE_USER_ID + " = ? AND " + COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(eventId)}
        );

        return rowsAffected > 0;
    }

    public boolean incrementEventAttendingCount(int eventId) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_ATTENDING_COUNT},
                COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int currentCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ATTENDING_COUNT));
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_ATTENDING_COUNT, currentCount + 1);

            int rowsAffected = db.update(
                    TABLE_EVENTS,
                    values,
                    COLUMN_EVENT_ID + " = ?",
                    new String[]{String.valueOf(eventId)}
            );

            return rowsAffected > 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    public boolean hasFreePlaces(int eventId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_PROMOTED, COLUMN_EVENT_CAPACITY, COLUMN_EVENT_ATTENDING_COUNT},
                COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int promoted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_PROMOTED));
            int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_CAPACITY));
            int attendingCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ATTENDING_COUNT));
            cursor.close();

            if (promoted == 0) {
                return true;
            }

            return attendingCount < capacity;
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    //INTERESTED EVENTS ZA ISPIS
    public ArrayList<Event> readInterestedEventsForUser(String username) {
        ArrayList<Event> events = new ArrayList<Event>();

        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            return events;
        }

        SQLiteDatabase db = getReadableDatabase();

        Cursor attendanceCursor = db.query(
                TABLE_ATTENDANCE,
                new String[]{COLUMN_ATTENDANCE_EVENT_ID},
                COLUMN_ATTENDANCE_USER_ID + " = ? AND " + COLUMN_ATTENDANCE_STATUS + " = ?",
                new String[]{String.valueOf(userId), "ZAINTERESOVAN"},
                null,
                null,
                null
        );

        if (attendanceCursor != null) {
            while (attendanceCursor.moveToNext()) {
                int eventId = attendanceCursor.getInt(
                        attendanceCursor.getColumnIndexOrThrow(COLUMN_ATTENDANCE_EVENT_ID)
                );

                Cursor eventCursor = db.query(
                        TABLE_EVENTS,
                        null,
                        COLUMN_EVENT_ID + " = ?",
                        new String[]{String.valueOf(eventId)},
                        null,
                        null,
                        null
                );

                if (eventCursor != null && eventCursor.moveToFirst()) {
                    Event event = cursorToEvent(eventCursor);
                    events.add(event);
                    eventCursor.close();
                } else {
                    if (eventCursor != null) {
                        eventCursor.close();
                    }
                }
            }
            attendanceCursor.close();
        }

        return events;
    }

    //ATTENDING EVENTS ZA ISPIS
    public ArrayList<Event> readAttendingEventsForUser(String username) {
        ArrayList<Event> events = new ArrayList<Event>();

        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            return events;
        }

        SQLiteDatabase db = getReadableDatabase();

        Cursor attendanceCursor = db.query(
                TABLE_ATTENDANCE,
                new String[]{COLUMN_ATTENDANCE_EVENT_ID},
                COLUMN_ATTENDANCE_USER_ID + " = ? AND " + COLUMN_ATTENDANCE_STATUS + " = ?",
                new String[]{String.valueOf(userId), "PRISUSTVUJE"},
                null,
                null,
                null
        );

        if (attendanceCursor != null) {
            while (attendanceCursor.moveToNext()) {
                int eventId = attendanceCursor.getInt(
                        attendanceCursor.getColumnIndexOrThrow(COLUMN_ATTENDANCE_EVENT_ID)
                );

                Cursor eventCursor = db.query(
                        TABLE_EVENTS,
                        null,
                        COLUMN_EVENT_ID + " = ?",
                        new String[]{String.valueOf(eventId)},
                        null,
                        null,
                        null
                );

                if (eventCursor != null && eventCursor.moveToFirst()) {
                    Event event = cursorToEvent(eventCursor);
                    events.add(event);
                    eventCursor.close();
                } else {
                    if (eventCursor != null) {
                        eventCursor.close();
                    }
                }
            }
            attendanceCursor.close();
        }

        return events;
    }


    //RATINGS
    public boolean hasUserRatedEvent(int userId, int eventId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query(
                TABLE_RATINGS,
                new String[]{COLUMN_RATING_ID},
                COLUMN_RATING_USER_ID + " =? AND " + COLUMN_RATING_EVENT_ID + " = ? ",
                new String[]{String.valueOf(userId), String.valueOf(eventId)},
                null,
                null,
                null
        );
        boolean exists=false;
        if(cursor !=null){
            exists=cursor.moveToFirst();
            cursor.close();
        }
        return exists;
    }

    public long insertRating(int userId,int eventId, int rating){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING_USER_ID, userId);
        values.put(COLUMN_RATING_EVENT_ID,eventId);
        values.put(COLUMN_RATING_VALUE,rating);
        return db.insert(TABLE_RATINGS,null,values);
    }

    public void updateEventRatingData(int eventId) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT AVG(" + COLUMN_RATING_VALUE + "), COUNT(" + COLUMN_RATING_VALUE + ") " +
                        "FROM " + TABLE_RATINGS +
                        " WHERE " + COLUMN_RATING_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            double avgRating = cursor.getDouble(0);
            int ratingCount = cursor.getInt(1);
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_AVG_RATING, avgRating);
            values.put(COLUMN_EVENT_RATING_COUNT, ratingCount);

            db.update(
                    TABLE_EVENTS,
                    values,
                    COLUMN_EVENT_ID + " = ?",
                    new String[]{String.valueOf(eventId)}
            );
        } else {
            if (cursor != null) {
                cursor.close();
            }
        }
    }




}


