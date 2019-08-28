package com.lucy.calendarproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME = "users";
    public static final String TABLE_NAME2 = "groups";
    public static final String TABLE_NAME3 = "userGroups";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, calendarDates TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE groups (groupID INTEGER PRIMARY KEY AUTOINCREMENT, groupName TEXT, noUsers INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE userGroups (userGroupID INTEGER PRIMARY KEY AUTOINCREMENT, userID TEXT, groupID TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME3);
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
            // Error: user already exists
            return 0;
        }
    }

    public long addGroup(String groupName, int noUsers){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", groupName);
        contentValues.put("noUsers", noUsers);
        long res = db.insert("groups", null, contentValues);
        db.close();
        return res;
    }

    public long addUserGroupLink(int userID, int groupID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID", userID);
        contentValues.put("groupID", groupID);
        long res = db.insert("userGroups", null, contentValues);
        db.close();
        return res;
    }

    public void addDate(int userID, String dateToAdd){

        // Get the current dates the user has in their calendar
        String currentDates;
        String newDates;
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("users", new String[] { "calendarDates"}, "ID = ? ", new String[] {strUserID}, null, null, null);
        if (cursor.moveToFirst()) {
            try{
                currentDates = cursor.getString(0);
            }catch(Exception e){
                // User has no dates yet so currentDates is set here to blank string
                currentDates = "";
            }

            // Now append new date to currentDates separated with a comma and update table
            if(currentDates==null){
                currentDates = "";
            }

            if(currentDates.equals("")){
                newDates = dateToAdd;
            }else{
                newDates = currentDates+","+dateToAdd;
            }

            ContentValues cv = new ContentValues();
            cv.put("calendarDates",newDates);
            db.update(TABLE_NAME, cv, "ID = ?", new String[]{strUserID});


        } else {
            // Error - can't find user
        }

        cursor.close();
        db.close();
    }

    public void removeDate(int userID, String dateToRemove){

        // Get the current dates the user has in their calendar
        String currentDates;
        String newDates="";
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("users", new String[] { "calendarDates"}, "ID = ? ", new String[] {strUserID}, null, null, null);
        if (cursor.moveToFirst()) {
            try{
                currentDates = cursor.getString(0);
            }catch(Exception e){
                // User has no dates yet so currentDates is set here to blank string
                currentDates = "";
            }

            if(currentDates.contains(dateToRemove)){
                // Need to find index of where data appears so that the comma before it can be deleted also
                int dateIndex = currentDates.indexOf(dateToRemove);

                if(dateIndex==0){
                    // Date to remove is first in string

                    try {
                        if (currentDates.charAt(dateToRemove.length()) == ',') {
                            String removedComma = new StringBuilder(currentDates).deleteCharAt(dateToRemove.length()).toString();
                            newDates = removedComma.replace(dateToRemove, "");
                        }
                    }catch(Exception e){
                        // Date is the only one in calendar so there is no comma to remove
                        newDates = currentDates.replace(dateToRemove, "");
                    }


                }else {
                    String removedComma = new StringBuilder(currentDates).deleteCharAt(dateIndex - 1).toString();
                    newDates = removedComma.replace(dateToRemove, "");
                }


            }else{
                // Data remains unchanged
                newDates = currentDates;
            }


            ContentValues cv = new ContentValues();
            cv.put("calendarDates",newDates);
            db.update(TABLE_NAME, cv, "ID = ?", new String[]{strUserID});


        } else {
            // Error - can't find user
        }

        cursor.close();
        db.close();
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

    public boolean checkGroupFree(String groupName){
        Boolean exists;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("groups", new String[] { "groupID"}, "groupName = ? ", new String[] {groupName}, null, null, null);
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
            // Error - can't find user
            userID = 0;
        }

        cursor.close();
        db.close();
        return userID;
    }

    public Integer getGroupID(String groupName){
        int groupID;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("groups", new String[] { "groupID"}, "groupName = ? ", new String[] {groupName}, null, null, null);
        if (cursor.moveToFirst()) {
            groupID = cursor.getInt(0);
        } else {
            // Error - can't find group
            groupID = 0;
        }

        cursor.close();
        db.close();
        return groupID;
    }

    public String getUsername(int userID){
        String Username;
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "username"}, "ID = ? ", new String[] {strUserID}, null, null, null);
        if (cursor.moveToFirst()) {
            Username = cursor.getString(0);
        } else {
            // Error - can't find user
            Username = "";
        }

        cursor.close();
        //db.close();
        return Username;
    }

    public String getCalendarDates(int userID){
        String calendarDates;
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "calendarDates"}, "ID = ? ", new String[] {strUserID}, null, null, null);
        if (cursor.moveToFirst()) {
            calendarDates = cursor.getString(0);
        } else {
            // Error - can't find user
            calendarDates = "";
        }

        cursor.close();
        db.close();
        return calendarDates;
    }



    public String getGroupName(int groupID){
        String groupName;
        String strGroupID = Integer.toString(groupID);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("groups", new String[] { "groupName"}, "groupID = ? ", new String[] {strGroupID}, null, null, null);
        if (cursor.moveToFirst()) {
            groupName = cursor.getString(0);
        } else {
            // Error - can't find group
            groupName = "";
        }

        cursor.close();
        //db.close();
        return groupName;
    }

    public ArrayList<String> getGroupsOfCurrentUser(int userID){
        ArrayList<String> usersGroups = new ArrayList<String>();
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("userGroups", new String[] { "groupID"}, "userID = ? ", new String[] {strUserID}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int usersGroupID = cursor.getInt(0);
            String groupName = getGroupName(usersGroupID);
            usersGroups.add(groupName);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return usersGroups;
    }

    public ArrayList<String> getUsersInGroup(int groupID){
        ArrayList<String> usersInGroup = new ArrayList<String>();
        String strGroupID = Integer.toString(groupID);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("userGroups", new String[] { "userID"}, "groupID = ? ", new String[] {strGroupID}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int userID = cursor.getInt(0);
            String username = getUsername(userID);
            usersInGroup.add(username);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return usersInGroup;
    }



    // Use this to delete records
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.close();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    // Use this to reset user's calendar
    public void resetCalendar(int userID){

        // Get the current dates the user has in their calendar
        String currentDates;
        String newDates;
        String strUserID = Integer.toString(userID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("users", new String[] { "calendarDates"}, "ID = ? ", new String[] {strUserID}, null, null, null);
        if (cursor.moveToFirst()) {
            try{
                currentDates = cursor.getString(0);
            }catch(Exception e){
                // User has no dates yet so currentDates is set here to blank string
                currentDates = "";
            }

            newDates = "";

            ContentValues cv = new ContentValues();
            cv.put("calendarDates",newDates);
            db.update(TABLE_NAME, cv, "ID = ?", new String[]{strUserID});


        } else {
            // Error - can't find user
        }

        cursor.close();
        db.close();
    }

    // Used 24/07 to change name from registeruser to users
    public void changeTableName(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" ALTER TABLE " + TABLE_NAME + " RENAME TO "+ "users");
        db.close();
    }

    // Used 22/08 to add calendarDates to users table
    public void addColumnToTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" ALTER TABLE " + TABLE_NAME + " ADD "+ "calendarDates TEXT");
        db.close();
    }

}
