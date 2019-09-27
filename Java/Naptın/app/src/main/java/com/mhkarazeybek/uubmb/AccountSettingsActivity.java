package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSettingsActivity extends AppCompatActivity {
   /* private BottomNavigationView mView;
    private FrameLayout frameLayout;
    private ChangePasswordFragment changePasswordFragment;
    private BlockedContactsFragment bcf;
    private BlockPersonFragment bpf;*/
   
    ListView listView;
    private FirebaseAuth mAuth;
    private ViewPager mVPager;
    private SectionsPagerAdapter mSecAd;
    private TabLayout mTab;
    private Toolbar toolbar;
    private FirebaseUser user;
    String new_pass,old_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //For Toolbar section
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hesap Ayarları");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mVPager = findViewById(R.id.main_view_tabs);
        mSecAd = new SectionsPagerAdapter(getSupportFragmentManager());
        mVPager.setAdapter(mSecAd);
        mTab = findViewById(R.id.main_tabs);
        mTab.setupWithViewPager(mVPager);



    }
    public void recovery(View view){
        
        user = FirebaseAuth.getInstance().getCurrentUser();
        old_pass = ChangePasswordFragment.oldpass.getEditText().getText().toString();
        new_pass = ChangePasswordFragment.newpass.getEditText().getText().toString();
        final String email = user.getEmail();
        if(TextUtils.isEmpty(old_pass) || TextUtils.isEmpty(new_pass)) {
            Toast.makeText(AccountSettingsActivity.this, "Alanları doldurunuz!", Toast.LENGTH_SHORT).show();
        }else {
            try {
                AuthCredential credential = EmailAuthProvider.getCredential(email, old_pass);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AccountSettingsActivity.this, "Hata,sonra tekrar deneyiniz!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AccountSettingsActivity.this, "Şifre değiştirildi", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ChatsActivity.class));
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AccountSettingsActivity.this, "Eski Şifrenizi kontrol edin!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void engKaldir(View view){
        String dizi[]=(String[])view.getTag();
        DatabaseReference friend1=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("blockedPeople").child(dizi[1]);
        friend1.removeValue();
    }
    public void blockPerson(View view){
        String dizi[]=(String[])view.getTag();
        DatabaseReference friend=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("friends").child(dizi[1]);
        friend.removeValue();
        DatabaseReference myFriend=FirebaseDatabase.getInstance().getReference().child("Naptin").child(dizi[1]).child("friends").child(mAuth.getUid());
        myFriend.removeValue();
        DatabaseReference friend1=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("blockedPeople").child(dizi[1]);
        friend1.child("name").setValue(dizi[2]);
    }
}
