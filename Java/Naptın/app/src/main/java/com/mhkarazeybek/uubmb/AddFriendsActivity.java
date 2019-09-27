package com.mhkarazeybek.uubmb;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriendsActivity extends Activity {


    ArrayList<String> friendNameFromFB;
    ArrayList<String> friendPhotoFromFB;
    ArrayList<String> friendUidFromFB;

    TextView addFriendsText;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    ListView listView;
    AddFriendAdapter adapter;
    String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        addFriendsText=findViewById(R.id.txtAddFriends);


        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        friendNameFromFB=new ArrayList<>();
        friendPhotoFromFB=new ArrayList<>();
        friendUidFromFB=new ArrayList<>();
        myName="null";
        getMyName();

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width= displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity=Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);


        adapter=new AddFriendAdapter(friendPhotoFromFB,friendNameFromFB,this);

        listView=findViewById(R.id.lstAddFriend);
    }

    public void search(View view) {
        String friendCode=addFriendsText.getText().toString().trim();
        if (friendCode.isEmpty()){
            Toast.makeText(this, "Boş Metin", Toast.LENGTH_SHORT).show();
        }else if(friendCode.contains("*")&&!(friendCode.length()<9)){
            getFriendUid(friendCode);
        }else {
            Toast.makeText(this, "Hatalı Giriş", Toast.LENGTH_SHORT).show();
        }
        if (friendNameFromFB!=null){
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    sendWants(friendUidFromFB.get(i));
                }
            });
        }
    }

    public void sendWants(final String uid){
        DatabaseReference wants=myRef.child("Naptin").child(uid).child("istekler").child(mAuth.getUid());
        wants.child("name").setValue(myName);
        wants.child("uid").setValue(mAuth.getUid());
        Toast.makeText(this, "İstek Gönderildi", Toast.LENGTH_SHORT).show();
        finish();
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

    public void getFriendUid(final String uid){
        try{
            final DatabaseReference newReference=myRef.child("Naptin").child("UUID");
            newReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).getValue()!=null && !dataSnapshot.child(uid).getValue().toString().equals(mAuth.getUid())){
                        getInfo(dataSnapshot.child(uid).getValue().toString());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getInfo(String uid){

        try{
            final DatabaseReference newReference=myRef.child("Naptin").child(uid);
            newReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendUidFromFB.clear();
                    friendNameFromFB.clear();
                    friendPhotoFromFB.clear();
                    friendNameFromFB.add(dataSnapshot.child("name").getValue().toString());
                    friendPhotoFromFB.add(dataSnapshot.child("Photo").getValue().toString());
                    friendUidFromFB.add(dataSnapshot.getKey());
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

    public void cancel(View view) {
        finish();
    }
}