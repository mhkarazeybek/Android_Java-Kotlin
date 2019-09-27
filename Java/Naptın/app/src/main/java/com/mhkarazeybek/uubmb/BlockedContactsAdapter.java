package com.mhkarazeybek.uubmb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlockedContactsAdapter extends ArrayAdapter {

    private final ArrayList<String> friendPhoto;
    private final ArrayList<String> friendName;
    private final ArrayList<String> friendUid;
    private final Activity context;



    public BlockedContactsAdapter(ArrayList<String> friendPhoto, ArrayList<String> friendName, ArrayList<String> friendUid, Activity context){
        super(context,R.layout.custom_blocked_contacts_view, friendName);
        this.friendPhoto = friendPhoto;
        this.friendName = friendName;
        this.friendUid=friendUid;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_blocked_contacts_view,null,true);

        final ImageView approveImgV=customView.findViewById(R.id.imgCancelBlock);
        final TextView friendNameText=customView.findViewById(R.id.txtBlockedName);
        final ImageView friendPhotoImgV=customView.findViewById(R.id.imgBlockedPhoto);

        String[] dizi=new String[2];
        dizi[0]="canceled";
        dizi[1]=friendUid.get(position);
        approveImgV.setTag(dizi);
        friendNameText.setText(friendName.get(position));
        try {
            Picasso.get().load(friendPhoto.get(position)).into(friendPhotoImgV);
        }catch (Exception e){
            e.printStackTrace();
        }


        return customView;
    }

}
