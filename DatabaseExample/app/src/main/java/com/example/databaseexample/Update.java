package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Update extends AppCompatActivity
{
    EditText et_fname;
    EditText et_lname;
    Button btn_back;
    Button btn_update;
    User userPassed;

    Intent mainActivity;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        et_fname = findViewById(R.id.et_v_u_fname);
        et_lname = findViewById(R.id.et_v_u_lname);
        btn_back = findViewById(R.id.btn_v_u_back);
        btn_update = findViewById(R.id.btn_v_u_update);

        mainActivity = new Intent(Update.this, MainActivity.class);

        //Get the user passed to this intent
        Intent cameFrom = getIntent();
        userPassed = (User) cameFrom.getSerializableExtra("User");
        et_fname.setText(userPassed.getFname());
        et_lname.setText(userPassed.getLname());

        dbHelper = new DatabaseHelper(this);


        updateButtonEvent();
        backButtonEvent();
    }

    public void updateButtonEvent()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Change the information about the user that was passed
                userPassed.setFname(et_fname.getText().toString());
                userPassed.setLname(et_lname.getText().toString());

                //Pass the new updated user to update
                dbHelper.updateUser(userPassed);

                //Start main Activity
                startActivity(mainActivity);
            }
        });
    }

    public void backButtonEvent()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(mainActivity);
            }
        });
    }
}