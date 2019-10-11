package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.lucy.calendarproject.newLogin.hash;

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

                // Validation checks to ensure username is between 1 and 15 characters in length
                boolean userValid= true;
                if(user.length()>=16||user.length()==0){
                    Toast.makeText(Register.this, "Username must be between 1 and 15 characters in length", Toast.LENGTH_LONG).show();
                    userValid=false;
                }

                // Validation checks to ensure password is valid
                boolean hasUppercase = !pwd.equals(pwd.toLowerCase());
                boolean passwordValid= true;

                if(pwd.length()<=5){
                    Toast.makeText(Register.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                    passwordValid = false;
                }else if(!hasUppercase && !(pwd.matches(".*\\d.*"))){
                    Toast.makeText(Register.this, "Password must contain a capital letter or number", Toast.LENGTH_LONG).show();
                    passwordValid = false;
                }

                if(pwd.equals(cnf_pwd)&&passwordValid&&userValid){

                    // Hash password
                    String hashedPass = hash(pwd);
                    System.out.println("The hashed password is: "+hashedPass);

                    // Create new record in users database
                    background bg = new background(Register.this);
                    bg.execute("username", "password", user, hashedPass, "registerUser");

                }else if (!pwd.equals(cnf_pwd)){
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