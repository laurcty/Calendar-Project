package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    Button mButtonDelete;
    TextView mTextViewRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        //mButtonDelete = (Button) findViewById(R.id.button_deleteData);
        mTextViewRegister = (TextView) findViewById(R.id.textview_register);



        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                Boolean res = db.checkUser(user,pwd);


                if (res == true){
                    int userID = db.getUserID(user);
                    String strUserID = Integer.toString(userID);
                    //Toast.makeText(Login.this, "User ID is: "+userID, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent Intent = new Intent(Login.this, MainActivity.class);
                    Intent.putExtra("USER_ID", strUserID);
                    startActivity(Intent);
                }else{
                    Toast.makeText(Login.this, "Login error", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //uncomment this when you want to use a button to delete records in user table

        /*
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deletedRows = db.deleteData("2");
            }
        });
        */
    }


}
