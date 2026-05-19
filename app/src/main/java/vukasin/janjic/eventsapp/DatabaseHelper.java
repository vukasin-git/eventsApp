package vukasin.janjic.eventsapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "EventsApp.db";
    private static final int DATABASE_VERSION=1;

    //Users tabela
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASSWORD="lozinka";

    // EVENTS tabela
    public static final String TABLE_EVENTS = "Events";
    public static final String COLUMN_EVENT_ID = "id";
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
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL" + ");";

        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    public long insertUser(String username, String email, String hashedPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);

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
        SQLiteDatabase db = this.getWritableDatabase();
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
    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

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
}
