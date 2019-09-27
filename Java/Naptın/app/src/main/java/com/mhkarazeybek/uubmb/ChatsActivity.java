package com.mhkarazeybek.uubmb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ChatsActivity extends AppCompatActivity {

    ArrayList<String> friendNameFromFB;
    ArrayList<String> friendPhotoFromFB;
    ArrayList<String> friendUidFromFB,types,lastMessageFromFB,sortedUidFromFB;
    ArrayList<Long> datesFromFB,sortedDates;
    private static FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ChatsAdapter adapter;
    LocalDatabase localDatabase;
    static boolean myState;
    static boolean stateResult=false;


    ListView listView;


    @Override
    protected void onStop() {
        if (user !=null){
            setMyState("false");
            myState=false;
            System.out.println("************************************");
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("/////////////////////////////////////////");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.yeniMesaj){
            Intent intent=new Intent(getApplicationContext(),CCActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.yeniGrup){
            Intent intent =new Intent(getApplicationContext(),CreateGroupChatsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.istekListesi){
            Intent intent=new Intent(getApplicationContext(),WantsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.profilAyarlari){

            Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.hesapAyarlari){
            Intent intent=new Intent(getApplicationContext(),AccountSettingsActivity.class);
            startActivity(intent);

        }else if(item.getItemId()==R.id.addFriend){
            Intent intent=new Intent(getApplicationContext(),AddFriendsActivity.class);
            startActivity(intent);

        }else if (item.getItemId()==R.id.logOut){
            mAuth.signOut();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        
        friendNameFromFB=new ArrayList<>();
        friendPhotoFromFB=new ArrayList<>();
        friendUidFromFB=new ArrayList<>();
        types=new ArrayList<>();
        datesFromFB=new ArrayList<>();
        sortedDates=new ArrayList<>();
        lastMessageFromFB=new ArrayList<>();
        sortedUidFromFB=new ArrayList<>();
        localDatabase=new LocalDatabase(this);
        

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        getDataFromFirebase();

        adapter=new ChatsAdapter(friendPhotoFromFB,friendNameFromFB,sortedDates,lastMessageFromFB,this);

        listView=findViewById(R.id.lstChats);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                if (types.get(i).equals("normal")){
                    Intent intent=new Intent(getApplicationContext(),MessagesActivity.class);
                    intent.putExtra("uid",sortedUidFromFB.get(i));
                    intent.putExtra("name",friendNameFromFB.get(i));
                    startActivity(intent);
                }else if(types.get(i).equals("group")){
                    Intent intent=new Intent(getApplicationContext(),GMessagesActivity.class);
                    intent.putExtra("uid",sortedUidFromFB.get(i));
                    intent.putExtra("name",friendNameFromFB.get(i));
                    startActivity(intent);
                }
            }
        });

        if(!getMyState()){
            setMyState("true");
            myState=true;
        }
    }

    private void getDataFromFirebase() {
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(mAuth.getCurrentUser().getUid()).child("lastMessages");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendUidFromFB.clear();
                    friendNameFromFB.clear();
                    friendPhotoFromFB.clear();
                    types.clear();
                    datesFromFB.clear();
                    sortedUidFromFB.clear();
                    sortedDates.clear();
                    lastMessageFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        //friendNameFromFB.add(hashMap.get("name"));
                        //friendPhotoFromFB.add(hashMap.get("Photo"));
                        HashMap<String,String> hashMap=(HashMap<String,String>) ds.getValue();
                       // types.add(hashMap.get("type"));
                        if (hashMap.get("date")!=null) {
                            datesFromFB.add(Long.parseLong(String.valueOf(hashMap.get("date"))));
                            sortedDates.add(Long.parseLong(String.valueOf(hashMap.get("date"))));
                        }
                        friendUidFromFB.add(ds.getKey());
                        /*
                        lastMessageFromFB.add(hashMap.get("message"));
                        friendUidFromFB.add(ds.getKey());
                        getNameFromFB(ds.getKey());
                        getPhotoFromFB(ds.getKey());
                        */
                    }
                    Collections.sort(sortedDates,Collections.<Long>reverseOrder());
                    getSortedData();
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

    private void getSortedData() {
        for (int i=0;i<sortedDates.size();i++) {
            int b=datesFromFB.indexOf(sortedDates.get(i));
            getChatsFromFB(friendUidFromFB.get(b));
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

    public void getNameFromFB(String name){
        try{
            final DatabaseReference newRef=myRef.child("Naptin").child(name).child("name");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendNameFromFB.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getChatsFromFB(String uid){
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(mAuth.getCurrentUser().getUid()).child("lastMessages").child(uid);
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //friendNameFromFB.add(hashMap.get("name"));
                    //friendPhotoFromFB.add(hashMap.get("Photo"));
                    types.add(dataSnapshot.child("type").getValue().toString());
                    lastMessageFromFB.add(dataSnapshot.child("message").getValue().toString());
/*
                    if (dataSnapshot.child("date").getValue()!=null){
                        if (localDatabase.setLastMessagesToDB(dataSnapshot.getKey(),Long.parseLong(dataSnapshot.child("date").getValue().toString())
                                ,dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("type").getValue().toString())) {
                            Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                        }
                    }*/
                        /*
                    LocalDatabase.setChatsToDB(dataSnapshot.getKey(),Long.parseLong(dataSnapshot.child("date").getValue().toString()
                            ,dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("sender").getValue().toString()));
                            */
                    getNameFromFB(dataSnapshot.getKey());
                    getPhotoFromFB(dataSnapshot.getKey());
                    sortedUidFromFB.add(dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
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

    public void newMessage(View view) {
        Intent intent=new Intent(getApplicationContext(),CCActivity.class);
        startActivity(intent);
    }
    
    static boolean getMyState(){
        try{
            final DatabaseReference newRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("state");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue().toString().equals("true")){
                        stateResult=true;
                    }else{
                        stateResult=false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return stateResult;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    static void setMyState(String state){
        try{
            HashMap<String, Object> onlineStateMap = new HashMap<>();
            onlineStateMap.put("state",state);
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid());
            databaseReference.updateChildren(onlineStateMap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
