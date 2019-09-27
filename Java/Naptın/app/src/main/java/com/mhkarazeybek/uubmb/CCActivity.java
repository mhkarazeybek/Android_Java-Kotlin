package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CCActivity extends AppCompatActivity {

    ArrayList<String> friendNameFromFB;
    ArrayList<String> friendPhotoFromFB;
    ArrayList<String> friendUidFromFB;
    ArrayList<String> friendStatusFromFB;
    private FirebaseAuth mAuth;
    CCAdapter adapter;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc);

        friendNameFromFB=new ArrayList<>();
        friendPhotoFromFB=new ArrayList<>();
        friendUidFromFB=new ArrayList<>();
        friendStatusFromFB=new ArrayList<>();
        setTitle("Kişi Seç");

        mAuth=FirebaseAuth.getInstance();

        getDataFromFirebase();

        adapter=new CCAdapter(friendPhotoFromFB,friendNameFromFB,friendStatusFromFB,this);
        listView=findViewById(R.id.lstSelectPerson);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),MessagesActivity.class);
                intent.putExtra("uid",friendUidFromFB.get(i));
                intent.putExtra("name",friendNameFromFB.get(i));
                startActivity(intent);
            }
        });

    }

    private void getDataFromFirebase() {
        try{
            final DatabaseReference newReference=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getCurrentUser().getUid()).child("friends");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendUidFromFB.clear();
                    friendNameFromFB.clear();
                    friendPhotoFromFB.clear();
                    friendStatusFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        getName(ds.getKey());
                        friendUidFromFB.add(ds.getKey());
                        getStatusFromFB(ds.getKey());
                        getPhotoFromFB(ds.getKey());
                        adapter.notifyDataSetChanged();
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

    public void getStatusFromFB(String uid){
        try{
            final DatabaseReference newRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(uid).child("status");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendStatusFromFB.add(dataSnapshot.getValue().toString());
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
            final DatabaseReference newRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(uid).child("Photo");
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

    public void newGroup(View view) {
        Intent intent=new Intent(getApplicationContext(),CreateGroupChatsActivity.class);
        startActivity(intent);
    }
}
