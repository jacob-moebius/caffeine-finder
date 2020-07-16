package edu.miracostacollege.cs134.caffeinefinder.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    //TASK: DEFINE THE DATABASE VERSION AND NAME  (DATABASE CONTAINS MULTIPLE TABLES)
    public static final String DATABASE_NAME = "CaffeineFinder";
    private static final int DATABASE_VERSION = 1;

    //TASK: DEFINE THE FIELDS (COLUMN NAMES) FOR THE CAFFEINE LOCATIONS TABLE
    private static final String LOCATIONS_TABLE = "Locations";
    private static final String LOCATIONS_KEY_FIELD_ID = "_id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_CITY = "city";
    private static final String FIELD_STATE = "state";
    private static final String FIELD_ZIP_CODE = "zip_code";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_LATITUDE = "latitude";
    private static final String FIELD_LONGITUDE = "longitude";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createQuery = "CREATE TABLE " + LOCATIONS_TABLE + "("
                + LOCATIONS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT, "
                + FIELD_ADDRESS + " TEXT, "
                + FIELD_CITY + " TEXT,"
                + FIELD_STATE + " TEXT,"
                + FIELD_ZIP_CODE + " TEXT,"
                + FIELD_PHONE + " TEXT,"
                + FIELD_LATITUDE + " REAL,"
                + FIELD_LONGITUDE + " REAL"
                + ")";
        database.execSQL(createQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE);

        onCreate(database);
    }

    //********** LOCATIONS TABLE OPERATIONS:  ADD, GETALL, DELETE

    public void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_NAME, location.getName());
        values.put(FIELD_ADDRESS, location.getAddress());
        values.put(FIELD_CITY, location.getCity());
        values.put(FIELD_STATE, location.getState());
        values.put(FIELD_ZIP_CODE, location.getZipCode());
        values.put(FIELD_PHONE, location.getPhone());
        values.put(FIELD_LATITUDE, location.getLatitude());
        values.put(FIELD_LONGITUDE, location.getLongitude());

        long id = db.insert(LOCATIONS_TABLE, null, values);
        location.setId(id);
        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public List<Location> getAllCaffeineLocations() {
        ArrayList<Location> locationsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                LOCATIONS_TABLE,
                new String[]{LOCATIONS_KEY_FIELD_ID, FIELD_NAME, FIELD_ADDRESS, FIELD_CITY, FIELD_STATE, FIELD_ZIP_CODE, FIELD_PHONE, FIELD_LATITUDE, FIELD_LONGITUDE},
                null,
                null,
                null, null, null, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()) {
            do {
                Location location =
                        new Location(cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getDouble(7),
                                cursor.getDouble(8));
                locationsList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return locationsList;
    }

    public void deleteLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(LOCATIONS_TABLE, LOCATIONS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(location.getId())});
        db.close();
    }

    public void deleteAllLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATIONS_TABLE, null, null);
        db.close();
    }

    public Location getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                LOCATIONS_TABLE,
                new String[]{LOCATIONS_KEY_FIELD_ID, FIELD_NAME, FIELD_ADDRESS, FIELD_CITY, FIELD_STATE, FIELD_ZIP_CODE, FIELD_PHONE, FIELD_LATITUDE, FIELD_LONGITUDE},
                LOCATIONS_KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Location location =
                new Location(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getDouble(7),
                        cursor.getDouble(8));
        cursor.close();
        db.close();
        return location;
    }

    public boolean importLocationsFromCSV(String csvFileName) {
        AssetManager manager = mContext.getAssets();
        InputStream inStream;
        try {
            inStream = manager.open(csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 9) {
                    Log.d("Caffeine Locations", "Skipping Bad CSV Row: " + Arrays.toString(fields));
                    continue;
                }
                long id = Long.parseLong(fields[0].trim());
                String name = fields[1].trim();
                String address = fields[2].trim();
                String city = fields[3].trim();
                String state = fields[4].trim();
                String zipCode = fields[5].trim();
                String phone = fields[6].trim();
                double latitude = Double.parseDouble(fields[7].trim());
                double longitude = Double.parseDouble(fields[8].trim());
                addLocation(new Location(id, name, address, city, state, zipCode, phone, latitude, longitude));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
