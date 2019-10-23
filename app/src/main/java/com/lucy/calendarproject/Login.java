package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Login extends AppCompatActivity implements AsyncTaskListener{

    EditText txtPass,txtUser;
    Button loginBtn;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUser = (EditText) findViewById(R.id.username);
        txtPass = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.button_login);
        textViewRegister = (TextView) findViewById(R.id.textview_register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = txtUser.getText().toString();
                String pass = txtPass.getText().toString();

                // Hash password
                String hashedPass = hash(pass);
                System.out.println("The hashed password is: "+hashedPass);

                // Set up connection and return result via AsyncTaskListener to updateResult method
                background bg = new background(Login.this);
                bg.execute("username","password",user,hashedPass,"login");
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void changeActivity(String result){
        System.out.println("connMsg is: "+result);
        if(result.contains("Login successful")) {
            Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent Intent = new Intent(Login.this, MainActivity.class);
            Intent.putExtra("USERNAME", txtUser.getText().toString());
            startActivity(Intent);
        }else{
            Toast.makeText(Login.this, "Username or password not correct", Toast.LENGTH_SHORT).show();
        }
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        changeActivity(result);
    }

    private static String hash (String plainText){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[]data = messageDigest.digest(plainText.getBytes());
            BigInteger bigInteger = new BigInteger(1,data);
            return bigInteger.toString(16);
        }catch(Exception e){
            System.out.println("Hashing failed");
            return null;
        }
    }
}
