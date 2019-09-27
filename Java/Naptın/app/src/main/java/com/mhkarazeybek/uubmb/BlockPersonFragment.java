package com.mhkarazeybek.uubmb;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockPersonFragment extends Fragment {

    private View mView;
    ListView listView;
    BlockPersonAdapter adapter;
    ArrayList<String> blockedPersonName;
    ArrayList<String> blockedPersonPhoto;
    ArrayList<String> blockedPersonUid;
    private FirebaseAuth mAuth;


    public BlockPersonFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_block_person, container, false);
        blockedPersonName=new ArrayList<>();
        blockedPersonPhoto=new ArrayList<>();
        blockedPersonUid=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();

        getDataFromFirebase();

        adapter=new BlockPersonAdapter(blockedPersonPhoto,blockedPersonName,blockedPersonUid,getActivity());
        listView=mView.findViewById(R.id.lstBlockPerson);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        return mView;


    }

    private void getDataFromFirebase() {
        try{
            final DatabaseReference newReference=FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getCurrentUser().getUid()).child("friends");
            newReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    blockedPersonUid.clear();
                    blockedPersonName.clear();
                    blockedPersonPhoto.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        getName(ds.getKey());
                        blockedPersonUid.add(ds.getKey());
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
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getName(String uid){
        try{
            final DatabaseReference newRef=FirebaseDatabase.getInstance().getReference().child("Naptin").child(uid).child("name");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    blockedPersonName.add(dataSnapshot.getValue().toString());
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
                    blockedPersonPhoto.add(dataSnapshot.getValue().toString());
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



}
