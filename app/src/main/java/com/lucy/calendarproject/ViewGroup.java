package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);

        //get the ID (corresponding to position in radioGroup) of the chosen group
        String groupID = getIntent().getStringExtra("ID_OF_GROUP");
        TextView groupIDText = (TextView) findViewById(R.id.GroupID);
        groupIDText.setText("Group: " + groupID);

    }
}
