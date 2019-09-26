package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class newLogin extends AppCompatActivity implements AsyncTaskListener{

    EditText pas,usr;
    Button loginBtn;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        usr = (EditText) findViewById(R.id.username);
        pas = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.button_login);
        textViewRegister = (TextView) findViewById(R.id.textview_register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usr.getText().toString();
                String pass = pas.getText().toString();

                // Set up connection and return result via AsyncTaskListener to updateResult method
                background bg = new background(newLogin.this);
                bg.execute("username","password",user,pass,"login");
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(newLogin.this, Register.class));
            }
        });
    }

    public void changeActivity(String result){
        System.out.println("connMsg is: "+result);
        if(result.contains("Login successful")) {
            Toast.makeText(newLogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent Intent = new Intent(newLogin.this, MainActivity.class);
            Intent.putExtra("USERNAME", usr.getText().toString());
            startActivity(Intent);
        }else{
            Toast.makeText(newLogin.this, "Username or password not correct", Toast.LENGTH_SHORT).show();
        }
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        changeActivity(result);
    }
}
