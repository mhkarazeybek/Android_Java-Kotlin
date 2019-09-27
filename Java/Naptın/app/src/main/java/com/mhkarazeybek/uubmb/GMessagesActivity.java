package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GMessagesActivity extends AppCompatActivity {

    ArrayList<String> messagesFromFB,sendersFromFB,senderNameFromFB;
    ArrayList<Long> datesFromFB;
    String targets="";


    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    GMessagesAdapter adapter;
    String uid;
    String name;

    EditText sendMessageText;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmessages);


        uid=getIntent().getStringExtra("uid");
        name=getIntent().getStringExtra("name");
        setTitle(name);
        messagesFromFB=new ArrayList<>();
        datesFromFB=new ArrayList<>();
        sendersFromFB=new ArrayList<>();
        senderNameFromFB=new ArrayList<>();


        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        mAuth=FirebaseAuth.getInstance();
        sendMessageText=findViewById(R.id.txtSendMessages);
        getTargets();
        getDataFromFirebase();

        adapter=new GMessagesAdapter(messagesFromFB,datesFromFB,sendersFromFB,senderNameFromFB,this);
        listView=findViewById(R.id.lstGMessages);
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
                    senderNameFromFB.clear();
                    sendersFromFB.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        HashMap<String,String> hashMap=(HashMap<String,String>) ds.getValue();
                        messagesFromFB.add(hashMap.get("message"));
                        sendersFromFB.add(hashMap.get("sender"));
                        getName(hashMap.get("sender"));
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

    public void sendGMessage(View view) {
        if (sendMessageText.getText().toString().isEmpty()){
            Toast.makeText(this, "Boş Mesaj", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference sendMessageO=myRef.child("Naptin").child(mAuth.getUid()).child("sohbetler").child(uid).child("messages").push();
            DatabaseReference sendMessageI=myRef.child("Naptin").child(uid).child("sohbetler").child("messages").push();
            DatabaseReference lastMessage=myRef.child("Naptin").child(mAuth.getUid()).child("lastMessages").child(uid);

            lastMessage.child("message").setValue(sendMessageText.getText().toString());
            lastMessage.child("date").setValue(ServerValue.TIMESTAMP);
            lastMessage.child("chatId").setValue(uid);
            lastMessage.child("type").setValue("group");
            lastMessage.child("sender").setValue(mAuth.getUid());

            sendMessageO.child("message").setValue(sendMessageText.getText().toString());
            sendMessageO.child("date").setValue(ServerValue.TIMESTAMP);
            sendMessageO.child("sender").setValue(mAuth.getUid());
            sendMessageI.child("message").setValue(sendMessageText.getText().toString());
            sendMessageI.child("date").setValue(ServerValue.TIMESTAMP);
            sendMessageI.child("sender").setValue(mAuth.getUid());
            sendMessagesOtherPerson();
            sendMessageText.setText("");

        }
    }

    public void sendMessagesOtherPerson(){

        for (String item:targets.split(",")) {
            if (item!=null && item!="" && !item.equals(mAuth.getUid())){
                DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(item).child("sohbetler").child(uid).child("messages").push();
                DatabaseReference lastMessage=FirebaseDatabase.getInstance().getReference().child("Naptin").child(item).child("lastMessages").child(uid);
                lastMessage.child("message").setValue(sendMessageText.getText().toString());
                lastMessage.child("date").setValue(ServerValue.TIMESTAMP);
                lastMessage.child("chatId").setValue(uid);
                lastMessage.child("type").setValue("group");
                lastMessage.child("sender").setValue(mAuth.getUid());

                myRef.child("message").setValue(sendMessageText.getText().toString());
                myRef.child("date").setValue(ServerValue.TIMESTAMP);
                myRef.child("sender").setValue(mAuth.getUid());
            }
        }

    }

    public void getTargets(){
        try{
            final DatabaseReference newReference=firebaseDatabase.getReference("Naptin").child(uid).child("target");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    targets=dataSnapshot.getValue().toString();
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
    public void getName(String uid){

        try{
            final DatabaseReference newReference=FirebaseDatabase.getInstance().getReference("Naptin").child(uid).child("name");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    senderNameFromFB.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, ChatsActivity.class));
    }
}
