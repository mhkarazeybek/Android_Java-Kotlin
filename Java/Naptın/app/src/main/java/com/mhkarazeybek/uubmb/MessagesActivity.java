package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesActivity extends AppCompatActivity {

    ArrayList<String> messagesFromFB,sendersFromFB;
    ArrayList<Long> datesFromFB;


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    MessagesAdapter adapter;
    String uid;
    String name;

    EditText sendMessageText;
    ListView listView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        uid=getIntent().getStringExtra("uid");
        name=getIntent().getStringExtra("name");
        //setTitle(name);
        messagesFromFB=new ArrayList<>();
        datesFromFB=new ArrayList<>();
        sendersFromFB=new ArrayList<>();

        toolbar = findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        if (ChatsActivity.myState){
            getSupportActionBar().setSubtitle("çevrimiçi");
        }else {
            getSupportActionBar().setSubtitle("çevrimdışı");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        sendMessageText=findViewById(R.id.txtMessage);
        getDataFromFirebase();

        adapter=new MessagesAdapter(messagesFromFB,datesFromFB,sendersFromFB,this);
        listView=findViewById(R.id.lstMessages);
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount()-1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference mRef=firebaseDatabase.getReference("Naptin").child("tarih");
                mRef.setValue(ServerValue.TIMESTAMP);
            }
        });

    }

    public void getDataFromFirebase(){
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(mAuth.getCurrentUser().getUid()).child("sohbetler").child(uid).child("messages");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    messagesFromFB.clear();
                    datesFromFB.clear();
                    sendersFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        HashMap<String,String> hashMap=(HashMap<String,String>) ds.getValue();
                        messagesFromFB.add(hashMap.get("message"));
                        sendersFromFB.add(hashMap.get("sender"));
                        if (hashMap.get("date")!=null) {
                            datesFromFB.add(Long.parseLong(String.valueOf(hashMap.get("date"))));
                        }
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

    public void sendMessage(View view) {
        if (sendMessageText.getText().toString().isEmpty()){
            Toast.makeText(this, "Boş Mesaj", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference sendMessageO=myRef.child("Naptin").child(mAuth.getUid()).child("sohbetler").child(uid).child("messages").push();
            DatabaseReference sendMessageI=myRef.child("Naptin").child(uid).child("sohbetler").child(mAuth.getUid()).child("messages").push();
            DatabaseReference lastMessageI=myRef.child("Naptin").child(uid).child("lastMessages").child(mAuth.getUid());
            DatabaseReference lastMessageO=myRef.child("Naptin").child(mAuth.getUid()).child("lastMessages").child(uid);

            lastMessageI.child("message").setValue(sendMessageText.getText().toString());
            lastMessageI.child("date").setValue(ServerValue.TIMESTAMP);
            lastMessageI.child("chatId").setValue(mAuth.getUid());
            lastMessageI.child("type").setValue("normal");
            lastMessageI.child("sender").setValue(mAuth.getUid());

            lastMessageO.child("message").setValue(sendMessageText.getText().toString());
            lastMessageO.child("date").setValue(ServerValue.TIMESTAMP);
            lastMessageO.child("chatId").setValue(uid);
            lastMessageO.child("type").setValue("normal");
            lastMessageO.child("sender").setValue(mAuth.getUid());

            sendMessageO.child("message").setValue(sendMessageText.getText().toString());
            sendMessageO.child("date").setValue(ServerValue.TIMESTAMP);
            sendMessageO.child("sender").setValue(mAuth.getUid());
            sendMessageI.child("message").setValue(sendMessageText.getText().toString());
            sendMessageI.child("date").setValue(ServerValue.TIMESTAMP);
            sendMessageI.child("sender").setValue(mAuth.getUid());
            sendMessageText.setText("");

        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, ChatsActivity.class));
    }

    @Override
    protected void onStop() {
        if (user!=null){
            ChatsActivity.setMyState("false");
            ChatsActivity.myState=false;
        }
        super.onStop();
    }

}
