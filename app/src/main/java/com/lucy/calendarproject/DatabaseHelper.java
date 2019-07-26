package com.lucy.calendarproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(String user, String password){
        Boolean free = checkUserFree(user);
        if (free) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", user);
            contentValues.put("password", password);
            long res = db.insert("users", null, contentValues);
            db.close();
            return res;
        }else{
            //error: user already exists
            return 0;
        }
    }

    public boolean checkUserFree(String username){
        Boolean exists;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "ID"}, "username = ? ", new String[] {username}, null, null, null);
        if (cursor.moveToFirst()) {
            exists=false;
        } else {
            exists=true;
        }

        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUser(String username, String password){
        String [] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + "and " + COL_3 + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0){
            return true;
        }else{
            return false;
        }
    }

    public Integer getUserID(String username){
        int userID;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "ID"}, "username = ? ", new String[] {username}, null, null, null);
        if (cursor.moveToFirst()) {
            userID = cursor.getInt(0);
        } else {
            //error - can't find user
            userID = 0;
        }

        cursor.close();
        db.close();
        return userID;
    }

    //use this to delete records, call by taking button out of comments in login.xml
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }


    //used 24/07 to change name from registeruser to users, keeping here just in case
    public void changeTableName(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" ALTER TABLE " + TABLE_NAME + " RENAME TO "+ "users");
        db.close();
    }

}
