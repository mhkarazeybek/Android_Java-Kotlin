package com.mhkarazeybek.uubmb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WantsActivity extends AppCompatActivity {

    ArrayList<String> wantNameFromFB;
    ArrayList<String> wantPhotoFromFB;
    ArrayList<String> wantUidFromFB;

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    WantsAdapter adapter;
    String myName;

    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wants);
        setTitle("İstek Listesi");

        wantNameFromFB=new ArrayList<>();
        wantPhotoFromFB=new ArrayList<>();
        wantUidFromFB=new ArrayList<>();
        myName="null";
        getMyName();

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        mAuth=FirebaseAuth.getInstance();

        getDataFromFirebase();

        adapter=new WantsAdapter(wantNameFromFB,wantPhotoFromFB,wantUidFromFB,this);

        listView=findViewById(R.id.lstWants);
        listView.setAdapter(adapter);
    }

    private void getDataFromFirebase() {
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(mAuth.getCurrentUser().getUid()).child("istekler");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    wantUidFromFB.clear();
                    wantNameFromFB.clear();
                    wantPhotoFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        HashMap<String,String> hashMap=(HashMap<String,String>) ds.getValue();
                        wantNameFromFB.add(hashMap.get("name"));
                        //friendPhotoFromFB.add(hashMap.get("Photo"));
                        wantUidFromFB.add(hashMap.get("uid"));
                        getPhotoFromFB(hashMap.get("uid"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getPhotoFromFB(String uid){
        try{
            final DatabaseReference newRef=myRef.child("Naptin").child(uid).child("Photo");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    wantPhotoFromFB.add(dataSnapshot.getValue().toString());
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void accept(View view){
        String dizi[]=(String[])view.getTag();
        DatabaseReference friend=myRef.child("Naptin").child(mAuth.getUid()).child("friends").child(dizi[1]);
        friend.child("name").setValue(dizi[0]);
        DatabaseReference myFriend=myRef.child("Naptin").child(dizi[1]).child("friends").child(mAuth.getUid());
        myFriend.child("name").setValue(myName);
        DatabaseReference friend1=myRef.child("Naptin").child(mAuth.getUid()).child("istekler").child(dizi[1]);
        friend1.removeValue();
        adapter.notifyDataSetChanged();
        
    }

    public void getMyName(){
        try{
            final DatabaseReference newReference=myRef.child("Naptin").child(mAuth.getUid());
            newReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    myName =dataSnapshot.child("name").getValue().toString();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancel(View view){
        String dizi[]=(String[])view.getTag();
        DatabaseReference friend=myRef.child("Naptin").child(mAuth.getUid()).child("istekler").child(dizi[1]);
        friend.removeValue();
        adapter.notifyDataSetChanged();
    }
}
