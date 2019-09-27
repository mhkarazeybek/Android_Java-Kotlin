package com.mhkarazeybek.uubmb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    TextView emailText;
    TextView passwordText;
    Button btnSignIn;
    int type=0;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText=findViewById(R.id.txtEmail);
        passwordText=findViewById(R.id.txtPassword);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null &&user.isEmailVerified()){
            Intent intent=new Intent(this,ChatsActivity.class);
            startActivity(intent);
            ChatsActivity.myState=true;
            ChatsActivity.setMyState("true");
        }

    }

    public void signIn(View view) {
       try{
           if (!emailText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
               mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                       .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   FirebaseUser user=mAuth.getCurrentUser();
                                   if (user.isEmailVerified()){
                                       Intent intent=new Intent(getApplicationContext(),ChatsActivity.class);
                                       startActivity(intent);
                                   }else {
                                       sendEmailVerification();
                                       Toast.makeText(LoginActivity.this, "E-posta Adresiniz Onaylanmamış", Toast.LENGTH_SHORT).show();
                                   }
                               } else {
                                   Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                           Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
           }else {
               Toast.makeText(this, "Tüm Alanlar dolu olmak zorunda", Toast.LENGTH_SHORT).show();
           }
       }catch (Exception e){
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           e.printStackTrace();
       }
    }

    public void signUp(View view) {
        Intent intent =new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);

    }

    private void sendEmailVerification(){
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void forgotPassword(View view) {
        Intent intent=new Intent(getApplicationContext(),PopUpActivity.class);
        startActivity(intent);
    }
}