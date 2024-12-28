package com.example.studywithme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "StudyWithMe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, otp TEXT)");
        db.execSQL("CREATE TABLE StudySessions(id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, subject TEXT, date TEXT, time TEXT, duration TEXT, description TEXT)");
        db.execSQL("CREATE TABLE Goals(id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, goal TEXT, dueDate TEXT, isCompleted INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS StudySessions");
        db.execSQL("DROP TABLE IF EXISTS Goals");
        onCreate(db);
    }

    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertStudySession(String subject, String date, String time, String duration, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", subject);
        values.put("date", date);
        values.put("time", time);
        values.put("duration", duration);
        values.put("description", description);
        long result = db.insert("StudySessions", null, values);
        return result != -1;
    }

    public boolean insertGoal(String goal, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goal", goal);
        values.put("dueDate", dueDate);
        values.put("isCompleted", 0); // Default to not completed
        long result = db.insert("Goals", null, values);
        return result != -1;
    }

    public boolean updateOtp(String email, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("otp", otp);
        int rowsAffected = db.update("Users", values, "email = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int rowsAffected = db.update("Users", values, "email = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public String getOtp(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT otp FROM Users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isValid;
    }

    public boolean deleteStudySession(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("StudySessions", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteGoal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Goals", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
