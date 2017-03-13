package com.example.personale.contactlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.personale.contactlist.model.Contact;

import java.util.ArrayList;

/**
 * Created by personale on 09/03/2017.
 */

public class DbHandler extends SQLiteOpenHelper{


    public DbHandler(Context context) {
        super(context, DbField.DBNAME, null, DbField.VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + DbField.TABLE + " ("
                + DbField.ID + " INTEGER PRIMARY KEY, "
                + DbField.NAME + " TEXT, "
                + DbField.ADDRESS + " TEXT, "
                + DbField.MAIL + " TEXT, "
                + DbField.PHONE + " TEXT); ";

        System.out.println(CREATE_TABLE);

        db.execSQL(CREATE_TABLE);
    }

    public int insert(Contact c){
        ContentValues values = new ContentValues();
        values.put(DbField.NAME, c.getName());
        values.put(DbField.ADDRESS, c.getAddress());
        values.put(DbField.PHONE, c.getPhone());
        values.put(DbField.MAIL, c.getMail());

        SQLiteDatabase db = this.getWritableDatabase();
        int res = (int)db.insert(DbField.TABLE, null, values);
        c.setId(res);

        return res;
    }

    public int edit(Contact c){
        String whereClause = DbField.ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(c.getId())};
        ContentValues values = new ContentValues();
        values.put(DbField.NAME, c.getName());
        values.put(DbField.ADDRESS, c.getAddress());
        values.put(DbField.PHONE, c.getPhone());
        values.put(DbField.MAIL, c.getMail());

        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.update(DbField.TABLE, values, whereClause, whereArgs);
        db.close();

        return res;
    }

    public int delete(int id){
        String whereClause = DbField.ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(DbField.TABLE, whereClause, new String[]{String.valueOf(id)});
        db.close();

        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbField.TABLE);
        onCreate(db);
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DbField.TABLE, null);

        if(c.moveToFirst()){
            do{
                contacts.add(new Contact(
                        c.getString(c.getColumnIndex(DbField.NAME)),
                        c.getString(c.getColumnIndex(DbField.MAIL)),
                        c.getString(c.getColumnIndex(DbField.PHONE)),
                        c.getString(c.getColumnIndex(DbField.ADDRESS))
                ));
            }while(c.moveToNext());
        }

        db.close();

        return contacts;
    }
}
