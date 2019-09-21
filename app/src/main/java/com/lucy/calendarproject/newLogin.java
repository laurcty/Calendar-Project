package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class newLogin extends AppCompatActivity implements AsyncTaskListener{


    EditText pas,usr;
    String connMsg;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        usr = (EditText) findViewById(R.id.username);
        pas = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.button_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usr.getText().toString();
                String pass = pas.getText().toString();

                // Set up connection and return result via AsyncTaskListener to updateResult method
                background bg = new background(newLogin.this);
                bg.execute("username","password",user,pass);

                /*
                synchronized(bg) {
                    try {
                        System.out.println("Waiting for bg to complete...");
                        bg.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                */



                /*
                if(connMsg.contains("Login successful")) {
                    Toast.makeText(newLogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent Intent = new Intent(newLogin.this, MainActivity.class);
                    Intent.putExtra("USER_ID", "1"); //todo hardcoded temporarily to 1
                    startActivity(Intent);
                }
                */
            }
        });
    }

    public void changeActivity(String result){
        System.out.println("connMsg is: "+result);
        if(result.contains("Login successful")) {
            Toast.makeText(newLogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent Intent = new Intent(newLogin.this, MainActivity.class);
            Intent.putExtra("USER_ID", "1"); //todo hardcoded temporarily to 1
            startActivity(Intent);
        }
    }

    @Override
    public void updateResult(String result){
        //connMsg = result;
        System.out.println("I'm in the updateResult method!!!!!");
        //System.out.println("connMsg is: "+connMsg);
        changeActivity(result);
    }
}
