package com.mhkarazeybek.uubmb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Sign-up with firebase values
    private FirebaseAuth mAuth;

    private TextInputLayout musername,memail,mpassword,passwordagn;
    private Button mreg_btn;
    private ProgressDialog mRegProgress;
    //For Firebase data
    private DatabaseReference databaseReference;
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        setTitle("Hesap Oluştur");

        //For back icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegProgress = new ProgressDialog(this);

        //Sign-up fields
        musername = findViewById(R.id.reg_username);
        memail = findViewById(R.id.reg_email);
        mpassword = findViewById(R.id.reg_password);
        passwordagn = findViewById(R.id.reg_password_agn);
        mreg_btn = findViewById(R.id.reg_btn);

        try{
            final DatabaseReference newReference=FirebaseDatabase.getInstance().getReference().child("Naptin").child("UUID").child("uid");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    uid=Integer.parseInt(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }




        mreg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String User_name = musername.getEditText().getText().toString();
                String Email = memail.getEditText().getText().toString();
                String Password = mpassword.getEditText().getText().toString();
                String Password_agn = passwordagn.getEditText().getText().toString();

                if (!(Password.equals(Password_agn))){

                    Toast.makeText(RegisterActivity.this,"Şifre'yi kontrol et!",Toast.LENGTH_SHORT).show();


                }else if (!TextUtils.isEmpty(User_name) || !TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Password) || !TextUtils.isEmpty(Password_agn)){

                    mRegProgress.setTitle("Hesap oluşturuluyor");
                    mRegProgress.setMessage("Biraz bekleyiniz");
                    mRegProgress.show();

                    //make Method
                    register(User_name,Email,Password);

                }else {
                    Toast.makeText(RegisterActivity.this,"Bilgilerini kontrol et!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/uubmb-50237.appspot.com/o/naptinLogo.png?alt=media&token=3663110b-33df-40b7-8814-a7e6097b6a23";

    private void register(final String user_name, final String email,final String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid());

                    //For complex data
                    HashMap<String,String> usermap = new HashMap<>();
                    usermap.put("name",user_name);
                    usermap.put("status","Hey,I am using Naptın :D");
                    usermap.put("Photo",downloadUrl);
                    databaseReference.setValue(usermap);
                    UserUidCode();
                    sendVerificationEmail();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);

                }else {
                    mRegProgress.hide();

                    Toast.makeText(RegisterActivity.this,"Hata!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void sendVerificationEmail(){
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Verification email sent to "+ user.getEmail(),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this,"Failed to sent email!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void UserUidCode() {


        String Uid = (mAuth.getUid()).substring(0, 4);
        int Uuid= uid+1;
        String UUID = Integer.toString(Uuid);

        if (Uuid >= 1 && Uuid <= 9) {

            Uid = Uid + "*000" + UUID;

        } else if (Uuid >= 10 && Uuid <= 99) {

            Uid = Uid + "*00" + UUID;

        } else if (Uuid >= 100 && Uuid <= 999) {

            Uid = Uid + "*0" + UUID;

        } else {

            Uid = Uid + "*" + UUID;

        }
        Toast.makeText(RegisterActivity.this, "Naptın Arkadaş Kodunuz:" + Uid, Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference().child("Naptin").child("UUID").child(Uid).setValue(mAuth.getUid());
        FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("naptinCode").setValue(Uid);
        FirebaseDatabase.getInstance().getReference().child("Naptin").child("UUID").child("uid").setValue(Uuid);
        }
}
