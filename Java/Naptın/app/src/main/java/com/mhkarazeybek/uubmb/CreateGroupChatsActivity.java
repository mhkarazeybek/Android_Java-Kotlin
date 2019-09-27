package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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

public class CreateGroupChatsActivity extends AppCompatActivity {

    ArrayList<String> friendNameFromFB;
    ArrayList<String> friendPhotoFromFB;
    ArrayList<String> friendUidFromFB;
    ArrayList<String> selectedPerson;

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    CreateGroupChatsAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chats);
        setTitle("Yeni Grup");

        friendNameFromFB=new ArrayList<>();
        friendPhotoFromFB=new ArrayList<>();
        friendUidFromFB=new ArrayList<>();
        selectedPerson=new ArrayList<>();

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        mAuth=FirebaseAuth.getInstance();

        getDataFromFirebase();

        adapter=new CreateGroupChatsAdapter(friendPhotoFromFB,friendNameFromFB,friendUidFromFB,this);
        listView=findViewById(R.id.lstCG);
        listView.setAdapter(adapter);
    }

    private void getDataFromFirebase() {
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(mAuth.getCurrentUser().getUid()).child("friends");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendUidFromFB.clear();
                    friendNameFromFB.clear();
                    friendPhotoFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        HashMap<String,String> hashMap=(HashMap<String,String>) ds.getValue();
                        getName(ds.getKey());
                        //friendPhotoFromFB.add(hashMap.get("Photo"));
                        friendUidFromFB.add(ds.getKey());
                        getPhotoFromFB(ds.getKey());
                        //adapter.notifyDataSetChanged();
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

    private void getName(String uid){
        try{
            final DatabaseReference newRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(uid).child("name");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendNameFromFB.add(dataSnapshot.getValue().toString());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPhotoFromFB(String uid){
        try{
            final DatabaseReference newRef=myRef.child("Naptin").child(uid).child("Photo");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendPhotoFromFB.add(dataSnapshot.getValue().toString());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void approve(View view) {
        ImageView imageView=(ImageView) view;
        String[] dizi= (String[]) view.getTag();

        if (dizi[0]=="unapproved"){
            imageView.setImageResource(R.drawable.cancel);
            selectedPerson.add(dizi[1]);
            dizi[0]="approved";
        }else {
            dizi[0]="unapproved";
            imageView.setImageResource(R.drawable.tick);
            selectedPerson.remove(dizi[1]);
        }
    }

    public void olustur(View view) {
        if (selectedPerson.size()<2){
            Toast.makeText(this, "En Az 2 Kişi Seçiniz", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent=new Intent(getApplicationContext(),CGSettingsActivity.class);
            intent.putExtra("people",selectedPerson);
            startActivity(intent);
        }
    }

}