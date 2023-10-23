package com.example.teamtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String  db_name = "database1.db";

    public DBHelper(@Nullable Context context) {
        super(context, db_name, null, 5);
        //context name, factory, version
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users " +
                "(id INTEGER PRIMARY KEY, " +
                "username TEXT, " +
                "password TEXT, " +
                "phone TEXT, " +
                "email TEXT, " +
                "latitude REAL, " +
                "longitude REAL)");


        db.execSQL("create table friend_requests " +
                "(user_id INTEGER, friend_id INTEGER, PRIMARY KEY (user_id, friend_id), " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (friend_id) REFERENCES users(id))");

        db.execSQL("create table friends " +
                "(user_id1 INTEGER, user_id2 INTEGER, PRIMARY KEY (user_id1, user_id2), " +
                "FOREIGN KEY (user_id1) REFERENCES users(id), " +
                "FOREIGN KEY (user_id2) REFERENCES users(id))");

    }

    public boolean sendFriendRequest(int id_username, int receiver_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if record already exists
        Cursor cursor = db.rawQuery("SELECT * FROM friend_requests WHERE user_id = ? AND friend_id = ?", new String[] {String.valueOf(id_username), String.valueOf(receiver_id)});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Record already exists, so return false
        }
        cursor.close();

        // Record does not exist, so insert new record
        ContentValues values = new ContentValues();
        values.put("user_id", id_username);
        values.put("friend_id", receiver_id);

        long result = db.insert("friend_requests", null, values);
        return result != -1; // Returns true if record was inserted successfully, false otherwise
    }

    public ArrayList<ArrayList<String>> getFriendRequestsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ArrayList<String>> requests = new ArrayList<ArrayList<String>>();

        ArrayList<String> user_id = new ArrayList<>();
        ArrayList<String> friend_id = new ArrayList<>();
        ArrayList<String> username = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT fr.user_id, fr.friend_id, " +
                "u1.username AS sender_username, u2.username AS receiver_username " +
                "FROM friend_requests fr " +
                "JOIN users u1 ON fr.user_id = u1.id " +
                "JOIN users u2 ON fr.friend_id = u2.id " +
                "WHERE fr.friend_id = ?", new String[] { String.valueOf(id) });

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                user_id.add(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))));
                friend_id.add(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("friend_id"))));
                username.add( cursor.getString(cursor.getColumnIndexOrThrow("sender_username") ) );

                cursor.moveToNext();
            }
        }
        cursor.close();

        //two dimensional array to hold both records
        requests.add(user_id);
        requests.add(friend_id);
        requests.add(username);

        return requests;
    }

    public void deleteFriendRequest(int user_id, int friend_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { String.valueOf(user_id), String.valueOf(friend_id) };
        db.delete("friend_requests", "user_id=? AND friend_id=?", args);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");

        db.execSQL("DROP TABLE IF EXISTS friend_requests");

        db.execSQL("DROP TABLE IF EXISTS friends");

        onCreate(db);

    }

    public boolean insertValues(String username, String password, String phone, String email , double latitude, double longitude){

        SQLiteDatabase db = this.getWritableDatabase(); //taku waylebkian btuanin shti teda bnusin, boya this bakardenin
        ContentValues inVals = new ContentValues();
        inVals.put("username", username);
        inVals.put("password", password);
        inVals.put("phone", phone);
        inVals.put("email", email);
        inVals.put("latitude", latitude);
        inVals.put("longitude", longitude);


        db.insert("users", null, inVals);

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor pointer = db.rawQuery("select * from users where id ="+ id ,null);
        return pointer;
    }

    public int numOfRow(){
        SQLiteDatabase db = this.getReadableDatabase();
        int noRows = (int) DatabaseUtils.queryNumEntries(db, "users");
        return noRows;
    }

    public void updateRecord(int id, String username, String password, String phone, String email ){
        //Integer id
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues upVals = new ContentValues();
        upVals.put("username", username);
        upVals.put("password", password);
        upVals.put("phone", phone);
        upVals.put("email", email);

        db.update("users", upVals, "id = ?", new String[]{ Integer.toString(id) } );
    }

    public Integer deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users", "id=?",new String[]{ Integer.toString(id) } );
    }

    public ArrayList<ArrayList<String>> getRecords() {
        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> phone = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor pointer = db.rawQuery(" select * from users", null);
        pointer.moveToFirst();

        while (pointer.isAfterLast() == false) {
            username.add(pointer.getString(pointer.getColumnIndexOrThrow("username")));
            password.add(pointer.getString(pointer.getColumnIndexOrThrow("password")));
            phone.add(pointer.getString(pointer.getColumnIndexOrThrow("phone")));
            email.add(pointer.getString(pointer.getColumnIndexOrThrow("email")));
            id.add(pointer.getString(pointer.getColumnIndexOrThrow("id")));


            pointer.moveToNext();
        }
        ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
        records.add(username);
        records.add(password);
        records.add(phone);
        records.add(email);
        records.add(id);

        return records;
    }

    public ArrayList<ArrayList<String>> searchRecordsFriendRequest(int idUser, String searchUsername ) {
        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> phone = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor pointer = db.rawQuery("SELECT * FROM users WHERE username LIKE ? AND id NOT LIKE ?", new String[]{"%" +searchUsername + "%", String.valueOf(idUser)});
        pointer.moveToFirst();

        while (pointer.isAfterLast() == false) {
            username.add(pointer.getString(pointer.getColumnIndexOrThrow("username")));
            password.add(pointer.getString(pointer.getColumnIndexOrThrow("password")));
            phone.add(pointer.getString(pointer.getColumnIndexOrThrow("phone")));
            email.add(pointer.getString(pointer.getColumnIndexOrThrow("email")));
            id.add(pointer.getString(pointer.getColumnIndexOrThrow("id")));


            pointer.moveToNext();
        }
        //two dimensional array to hold both records
        ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
        records.add(username);
        records.add(password);
        records.add(phone);
        records.add(email);
        records.add(id);


        return records;
    }


    public boolean insertFriends(int id_username, int receiver_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if record already exists
        Cursor cursor = db.rawQuery("SELECT * FROM friends WHERE user_id1 = ? AND user_id2 = ?", new String[] {String.valueOf(id_username), String.valueOf(receiver_id)});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Record already exists, so return false
        }
        cursor.close();

        // Record does not exist, so insert new record
        ContentValues values = new ContentValues();
        values.put("user_id1", id_username);
        values.put("user_id2", receiver_id);

        long result = db.insert("friends", null, values);
        return result != -1; // Returns true if record was inserted successfully, false otherwise
    }


    public ArrayList<String> getFriendsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> friends = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT u.username FROM users u " +
                "JOIN friends f ON f.user_id2 = u.id " +
                "WHERE f.user_id1 = ?", new String[] { String.valueOf(id) });

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                friends.add(username);
                cursor.moveToNext();
            }
        }
        cursor.close();

        Cursor cursor2 = db.rawQuery("SELECT u.username FROM users u " +
                "JOIN friends f ON f.user_id1 = u.id " +
                "WHERE f.user_id2 = ?", new String[] { String.valueOf(id) });

        if (cursor2.moveToFirst()) {
            while (!cursor2.isAfterLast()) {
                String username = cursor2.getString(cursor2.getColumnIndexOrThrow("username"));
                friends.add(username);
                cursor2.moveToNext();
            }
        }
        cursor2.close();

        return friends;
    }


    public void deleteFriends(int user_id1, int user_id2 ) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { String.valueOf(user_id1) , String.valueOf(user_id2)  };
        db.delete("friends", "user_id1=? and user_id2=?", args);
        db.close();
    }

    public ArrayList<ArrayList<String>> getFriendLocations(int userId) {
        ArrayList<ArrayList<String>> friendLocations = new ArrayList<ArrayList<String>>();

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> latitude = new ArrayList<>();
        ArrayList<String> longitude = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT users.id, users.username, users.latitude, users.longitude FROM users " +
                "INNER JOIN friends ON friends.user_id2 = users.id WHERE friends.user_id1 = " + userId, null);

        if (cursor.moveToFirst()) {
            do {

                id.add( cursor.getString(cursor.getColumnIndexOrThrow("id")));
                username.add( cursor.getString(cursor.getColumnIndexOrThrow("username")) );
                latitude.add( cursor.getString(cursor.getColumnIndexOrThrow("latitude")) );
                longitude.add( cursor.getString(cursor.getColumnIndexOrThrow("longitude")) );
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        friendLocations.add(id);
        friendLocations.add(username);
        friendLocations.add(latitude);
        friendLocations.add(longitude);

        return friendLocations;
    }

    public void updateLocation( int id, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues upVals = new ContentValues();
        upVals.put("latitude", latitude);
        upVals.put("longitude", longitude);

        db.update("users", upVals, "id=?", new String[]{ Integer.toString(id) } );
        db.close();
    }

}