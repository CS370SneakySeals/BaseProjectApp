// K
package sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import sqlite.model.Passcodes;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version (may not be necessary)
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "passcodes_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // CREATING TABLE WITH INITIAL QUESTIONS
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create passcodes table
        try {
            db.execSQL(Passcodes.CREATE_TABLE);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // get writable database as we want to write data
        //SQLiteDatabase db =
        this.getWritableDatabase();

        // populate it with initial 6 security questions
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        ContentValues values4 = new ContentValues();
        ContentValues values5 = new ContentValues();
        ContentValues values6 = new ContentValues();

        // passcode & answer should be empty
        // id auto-increments & doesn't need to be handled
        values1.put(Passcodes.COLUMN_QUESTION, "What was your first car?");
        values1.put(Passcodes.COLUMN_IS_SELECTED, false);
        values2.put(Passcodes.COLUMN_QUESTION, "Where do you like to vacation?");
        values2.put(Passcodes.COLUMN_IS_SELECTED, false);
        values3.put(Passcodes.COLUMN_QUESTION, "Who is your best friend?");
        values3.put(Passcodes.COLUMN_IS_SELECTED, false);
        values4.put(Passcodes.COLUMN_QUESTION, "What is your favorite sports team?");
        values4.put(Passcodes.COLUMN_IS_SELECTED, false);
        values5.put(Passcodes.COLUMN_QUESTION, "What is your spirit animal?");
        values5.put(Passcodes.COLUMN_IS_SELECTED, false);
        values6.put(Passcodes.COLUMN_QUESTION, "What is your favorite show?");
        values6.put(Passcodes.COLUMN_IS_SELECTED, false);

        // insert rows
        try{
            db.insert(Passcodes.TABLE_NAME, null, values1);
            db.insert(Passcodes.TABLE_NAME, null, values2);
            db.insert(Passcodes.TABLE_NAME, null, values3);
            db.insert(Passcodes.TABLE_NAME, null, values4);
            db.insert(Passcodes.TABLE_NAME, null, values5);
            db.insert(Passcodes.TABLE_NAME, null, values6);
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    // UPGRADING DATABASE (may not be necessary)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Passcodes.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // RETURNS PASSCODE OBJECT WITHOUT PASSCODE COLUMN
    public Passcodes getPasscodeEntry(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Passcodes.TABLE_NAME,
                new String[]{Passcodes.COLUMN_ID, Passcodes.COLUMN_QUESTION, Passcodes.COLUMN_ANSWER, Passcodes.COLUMN_IS_SELECTED},
                Passcodes.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Passcodes p = new Passcodes(
                cursor.getInt(cursor.getColumnIndex(Passcodes.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Passcodes.COLUMN_QUESTION)),
                cursor.getString(cursor.getColumnIndex(Passcodes.COLUMN_ANSWER)),
                cursor.getInt(cursor.getColumnIndex(Passcodes.COLUMN_IS_SELECTED)) == 1);

        // close the db connection
        cursor.close();

        return p;
    }

    // RETURNS PASSCODE STORED IN PASSCODE COLUMN ROW 1
    public int getPasscode() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Passcodes.TABLE_NAME,
                new String[]{Passcodes.COLUMN_PASSCODE},
                Passcodes.COLUMN_ID + "=?",
                new String[]{String.valueOf(1)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // store value of passcode
        int temp = cursor.getInt(cursor.getColumnIndex(Passcodes.COLUMN_PASSCODE));

        // close the db connection
        cursor.close();

        return temp;
    }

    // method compares value entered to passcode for match
    public boolean passcode_match(int entry){
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Passcodes.TABLE_NAME,
                new String[]{Passcodes.COLUMN_PASSCODE},
                Passcodes.COLUMN_ID + "=?",
                new String[]{String.valueOf(1)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // store value of passcode
        int temp = cursor.getInt(cursor.getColumnIndex(Passcodes.COLUMN_PASSCODE));

        // close the db connection
        cursor.close();

        return (temp == entry);
    }

    // passcode setter
    public int change_passcode(int entry){
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Passcodes.COLUMN_PASSCODE, entry);

        // updating row
        return db.update(Passcodes.TABLE_NAME, values, Passcodes.COLUMN_ID + " = ?",
                new String[]{String.valueOf(1)});
    }



}