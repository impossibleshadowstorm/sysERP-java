package com.dsvv.syserp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button signin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signin= (Button) findViewById(R.id.loginbtn);


        auth = FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = email.getText().toString();
                String pass = password.getText().toString();

                boolean isValidateSuccess = validation(user, pass);

                if(isValidateSuccess){
                    loginUser(user, pass);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public boolean validation(String user, String pass) {

        if(user.matches("")){
            Toast.makeText(MainActivity.this, "Enter Email !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            Toast.makeText(MainActivity.this, "Enter Valid Email !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(pass.matches("")){
            Toast.makeText(MainActivity.this, "Enter Password !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void loginUser(String username, String password) {

        auth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                finish();
            }
        });

        auth.signInWithEmailAndPassword(username, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
            }
        });

        auth.signInWithEmailAndPassword(username, password).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this, "Authorization Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }
}