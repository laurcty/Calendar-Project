package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity implements AsyncTaskListener{
    EditText textUsername;
    EditText textPassword;
    EditText textCnfPassword;
    Button buttonRegister;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        textUsername = (EditText)findViewById(R.id.edittext_username);
        textPassword = (EditText)findViewById(R.id.edittext_password);
        textCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        buttonRegister = (Button) findViewById(R.id.button_register);
        textViewLogin = (TextView) findViewById(R.id.textview_login);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = textUsername.getText().toString().trim();
                String pwd = textPassword.getText().toString().trim();
                String cnf_pwd = textCnfPassword.getText().toString().trim();
                //TODO hash and validate passwords

                if(pwd.equals(cnf_pwd)){
                    background bg = new background(Register.this);
                    bg.execute("username", "password", user, pwd, "registerUser");
                }else{
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        System.out.println(result);
        System.out.println("After printing result");
        if(result.contains("USERNAME TAKEN")){
            Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Register.this, "Successfully registered", Toast.LENGTH_SHORT).show();
            Intent moveToLogin = new Intent(Register.this, newLogin.class);
            startActivity(moveToLogin);
        }
    }
}