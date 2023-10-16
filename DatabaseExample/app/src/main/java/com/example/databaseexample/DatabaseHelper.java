package com.example.databaseexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "Users";

    public DatabaseHelper(Context context)
    {
        //We will use this to create the database
        //It accepts the context, the name, factory (leave null), and version number
        //If your database becomes corrupt or the information in the database is wrong,
        //you can change the version number
        //Super is used to call the functionality of the base class SQLiteOpenHelper and
        //then executes the extended class (DatabaseHelper)

        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create table in the database
        //Execute the SQL statement on the database that was passed to the function onCreate called db
        //This can be tricky because we have to write our SQL statements as strings

        //3 attributes: username, first name, and last name
        //All 3 attributes will be TEXT and username will be the primary key
        //*username has to be unique*
        //typically capitalize sql stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (username TEXT PRIMARY KEY NOT NULL, firstname TEXT, lastname TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        //This is use if we change the version number of the database

        //Delete the table if you upgrade the database (change the version number in the constructor)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");

        //Create a new table once the old table has been deleted
        onCreate(db);
    }

    //This is used to insert default info into the table
    public boolean initializeDB()
    {
        if(numberOfRowsInTable() == 0)
        {
            //Connect to the database
            //Notice we are getting a writable database because we need to insert information into our database
            SQLiteDatabase db = this.getWritableDatabase();

            //Execute insert statements
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Zmoore','Zackary','Moore');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('S_Thomas','Shannon','Thomas');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('BigG','Gabriel','Smith');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('HMoore','Harrison','Moore');");

            //Must close the database once we are done
            db.close();

            return true;
        }
        else {
            return false;
        }
    }

    public int numberOfRowsInTable()
    {
        //Look at the database we created
        //Get a readable version
        SQLiteDatabase db = this.getReadableDatabase();
        //Store the number of records in the table called TABLE_NAME
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);

        //Close the database
        db.close();

        return  numRows;
    }

    //Have it return an arrayList
    @SuppressLint("Range")
    public ArrayList<User> getAllRows()
    {
        //This will be used to store the info that the table returns
        ArrayList<User> listUsers = new ArrayList<User>();

        //Query to get all rows and attributes from our table
        //select * means get all attributes
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY username;";

        //Get an instance of a readable database and store it in db
        SQLiteDatabase db = this.getReadableDatabase();

        //Execute the query. Cursor will be used to cycle through the results
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Used to store attributes
        String fname;
        String lname;
        String uname;

        //If there was something returned, move the cursor to the beginning of the list
        if(cursor.moveToFirst())
        {
            do
            {
                //Find the username column from the returned results
                uname = cursor.getString(cursor.getColumnIndex("username")); //Red: click light bulb - Suppress Range

                //Find the firstname column from the returned results
                fname = cursor.getString(cursor.getColumnIndex("firstname"));

                //Find the lastname column from the returned results
                lname = cursor.getString(cursor.getColumnIndex("lastname"));

                //Add the returned results to my list
                listUsers.add(new User(uname, fname, lname));
            }
            while(cursor.moveToNext());
        }

        db.close();

        return listUsers;
    }

    public void addNewUser(User u)
    {
        //Get an instance of a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //This line is a little complicated. The sql statement should look as follows:
        //INSERT INTO users VALUES('zmoore','Zack','Moore'));
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES ('" + u.getUname() + "','" + u.getFname() + "','" + u.getLname() + "');");

        db.close();

    }

    @SuppressLint("Range")
    public ArrayList<String> getAllUsernames()
    {
        ArrayList<String> usernames = new ArrayList<String>();


        //Query to get all usernames from the table
        String selectUserNames = "SELECT username FROM " + TABLE_NAME + " ORDER BY username;";

        //Get instance of a readable database and store it in db
        SQLiteDatabase db = this.getReadableDatabase();

        //Execute query
        //Cursor will be used to cycle through the results
        Cursor cursor = db.rawQuery(selectUserNames, null);

        String username;

        //If there was something returned, move the cursor to the beginning of the list
        if(cursor.moveToFirst())
        {
            do
            {
                username = cursor.getString(cursor.getColumnIndex("username"));

                usernames.add(username);
            }
            while (cursor.moveToNext());
        }
        //CLOSE THE DATABASE
        db.close();
        return usernames;
    }

    //Used to delete a specific user
    //This will be passed a username because it is our primary key
    //You MUST delete off the primary key
    public void deleteUser(String uName)
    {
        //Get an instance of our database (writable)
        SQLiteDatabase db = this.getWritableDatabase();

        //Create our delete command
        //Looks like: DELETE FROM users WHERE username = 'zmoore';
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE username = '" + uName + "';");
        //CLOSE THE DATABASE
        db.close();
    }

    public void updateUser(User u)
    {
        //Get writeable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Create our update command
        //Needs to look like this:
        //UPDATE users SET firstname = 'Zack' , lastname = 'Moore' WHERE username = 'zmoore';
        String updateCommand = "UPDATE " + TABLE_NAME + " SET firstname = '" + u.getFname() + "' , lastname = '" + u.getLname() + "' WHERE username = '" + u.getUname() + "';";

        db.execSQL(updateCommand);
        db.close();
    }
}
