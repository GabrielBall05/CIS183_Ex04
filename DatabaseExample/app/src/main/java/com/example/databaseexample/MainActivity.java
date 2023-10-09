package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText et_j_fname;
    EditText et_j_lname;
    EditText et_j_uname;
    Button btn_j_addUser;
    ListView lv_j_users;
    ArrayList<User> userList;
    DatabaseHelper dbHelper;

    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_j_fname = findViewById(R.id.et_v_fname);
        et_j_lname = findViewById(R.id.et_v_lname);
        et_j_uname = findViewById(R.id.et_v_uname);
        btn_j_addUser = findViewById(R.id.btn_v_addUser);
        lv_j_users = findViewById(R.id.lv_v_users);

        userList = new ArrayList<User>();
        //Make an instance of the databaseHelper and pass it this
        dbHelper = new DatabaseHelper(this);
        //Call the initializeDB() function to fill the records into our table
        dbHelper.initializeDB();
        //test to make sure the records were inserted
        //We should see 4 when we run this
        Log.d("Number of records: ", dbHelper.numberOfRowsInTable() + "");

        userList = dbHelper.getAllRows();

        //For testing purposes:
        //displayUsers();

        //Get all of the usernames from our table
        usernames = dbHelper.getAllUsernames();

        //Remember that this is a simple menu, meaning that you can only display one string per cell
        //Will need to make a custom adapter and custom cell for the homework to display all info
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);

        //Tell the listview to use the adapter
        lv_j_users.setAdapter(adapter);

        addNewUserButtonEvent();
        deleteUserEvent();
    }

    public void addNewUserButtonEvent()
    {
        btn_j_addUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("Button Press", "Add User");

                String u = et_j_uname.getText().toString();
                String f = et_j_fname.getText().toString();
                String l = et_j_lname.getText().toString();

                User user = new User(u, f, l);

                //Adding user to the datase AND the arraylist
                addNewUser(user);

                //Add the username to the usernames arraylist
                usernames.add(u);
                //This line is easily forgotten. You need this line to so the listview will reflect the
                //new user based off the new username added to the usernames arraylist
                adapter.notifyDataSetChanged();

                //This is for testing only
                //displayUsers();

                //Clear text boxes
                et_j_uname.setText("");
                et_j_fname.setText("");
                et_j_lname.setText("");


            }
        });
    }

    public void addNewUser(User u)
    {
        //Add user to arraylist
        userList.add(u);
        //Add user to database
        dbHelper.addNewUser(u);
    }

    public void displayUsers()
    {
        for(int i = 0; i < userList.size(); i++)
        {
            Log.d("User: ", userList.get(i).getFname());
        }
    }

    public void deleteUserEvent()
    {
        lv_j_users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //Call the delete function in our dbHelper and pass it username
                dbHelper.deleteUser(usernames.get(i));

                //Remove the user from our userList and usernames arraylist
                userList.remove(i);
                usernames.remove(i);

                //Update the listview
                adapter.notifyDataSetChanged();


                return false;
            }
        });
    }

}