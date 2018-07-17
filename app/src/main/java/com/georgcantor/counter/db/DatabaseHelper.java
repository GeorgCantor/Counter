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
        db.execSQL(Counter.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Counter.TABLE_NAME);
        onCreate(db);
    }

    public long insertHistory(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Counter.COLUMN_TIME, time);

        long id = db.insert(Counter.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public Counter getCounter(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Counter.TABLE_NAME,
                new String[]{Counter.COLUMN_ID, Counter.COLUMN_TIME, Counter.COLUMN_TIMESTAMP},
                Counter.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToNext();
        }
        Counter counter = new Counter(cursor.getInt(cursor.getColumnIndex(Counter.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Counter.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(Counter.COLUMN_TIMESTAMP)));

        cursor.close();
        return counter;
    }

    public List<Counter> getAllCounter() {
        List<Counter> counterList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Counter.TABLE_NAME + " ORDER BY " +
                Counter.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Counter counter = new Counter();
                counter.setId(cursor.getInt(cursor.getColumnIndex(Counter.COLUMN_ID)));
                counter.setTime(cursor.getString(cursor.getColumnIndex(Counter.COLUMN_TIME)));
                counter.setTimestamp(cursor.getString(cursor.getColumnIndex(Counter.COLUMN_TIMESTAMP)));

                counterList.add(counter);
            } while (cursor.moveToNext());
        }
        db.close();
        return counterList;
    }

    public int getHistoryCount() {
        String countQuery = "SELECT  * FROM " + Counter.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateCounter(Counter counter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Counter.COLUMN_TIME, counter.getTime());

        return db.update(Counter.TABLE_NAME, values, Counter.COLUMN_ID + " = ?",
                new String[]{String.valueOf(counter.getId())});
    }

    public void deleteHistory(Counter counter) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Counter.TABLE_NAME, Counter.COLUMN_ID + " = ?",
                new String[]{String.valueOf(counter.getId())});
        db.close();
    }
}
