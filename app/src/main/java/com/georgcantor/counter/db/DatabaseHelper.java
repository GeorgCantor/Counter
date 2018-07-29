package com.georgcantor.counter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "counter_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(History.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + History.TABLE_NAME);
        onCreate(db);
    }

    public long insertHistory(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.COLUMN_TIME, time);

        long id = db.insert(History.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public History getHistory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(History.TABLE_NAME,
                new String[]{History.COLUMN_ID, History.COLUMN_TIME, History.COLUMN_TIMESTAMP},
                History.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToNext();
        }
        History history = new History(cursor.getInt(cursor.getColumnIndex(History.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_TIMESTAMP)));

        cursor.close();
        return history;
    }

    public List<History> getAllHistory() {
        List<History> historyList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + History.TABLE_NAME + " ORDER BY " +
                History.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setId(cursor.getInt(cursor.getColumnIndex(History.COLUMN_ID)));
                history.setTime(cursor.getString(cursor.getColumnIndex(History.COLUMN_TIME)));
                history.setTimestamp(cursor.getString(cursor.getColumnIndex(History.COLUMN_TIMESTAMP)));

                historyList.add(history);
            } while (cursor.moveToNext());
        }
        db.close();
        return historyList;
    }

    public int getHistoryCount() {
        String countQuery = "SELECT  * FROM " + History.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.COLUMN_TIME, history.getTime());

        return db.update(History.TABLE_NAME, values, History.COLUMN_ID + " = ?",
                new String[]{String.valueOf(history.getId())});
    }

    public void deleteHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(History.TABLE_NAME, History.COLUMN_ID + " = ?",
                new String[]{String.valueOf(history.getId())});
        db.close();
    }

    public void deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(History.TABLE_NAME, null, null);
        db.close();
    }
}
