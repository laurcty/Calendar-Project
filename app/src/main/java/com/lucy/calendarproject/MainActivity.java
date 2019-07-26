package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get userID
        String strUserID = getIntent().getStringExtra("USER_ID");
        //Toast.makeText(MainActivity.this, strUserID, Toast.LENGTH_SHORT).show();
        int userID = Integer.parseInt(strUserID);

        //change to viewGroup page when button clicked
        Button viewGroup = (Button) findViewById(R.id.viewGroup);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        viewGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int groupID = radioGroup.getCheckedRadioButtonId();
                if(groupID!=-1) {

                    String strGroupID = Integer.toString(groupID);
                    //Toast.makeText(MainActivity.this, Integer.toString(groupID), Toast.LENGTH_SHORT).show();
                    Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                    Intent.putExtra("ID_OF_GROUP", strGroupID);
                    startActivity(Intent);
                }else{
                    //error - no group selected
                    Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //change to createGroup page when button clicked
        Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateGroup.class));
            }
        });

        //change to ViewCalendar page when button clicked
        Button viewCalendar = (Button) findViewById(R.id.myCalendar);
        viewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewCalendar.class));
            }
        });
    }
}

