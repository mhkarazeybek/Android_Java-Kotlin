package com.mhkarazeybek.uubmb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddFriendAdapter extends ArrayAdapter {

    private final ArrayList<String> friendPhoto;
    private final ArrayList<String> friendName;
    private final Activity context;

    public AddFriendAdapter(ArrayList<String> friendPhoto, ArrayList<String> friendName, Activity context) {
        super(context,R.layout.custom_addfriend_view, friendName);
        this.friendPhoto = friendPhoto;
        this.friendName = friendName;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_addfriend_view,null,true);

        final TextView friendNameText=customView.findViewById(R.id.txtAddFriendName);
        final ImageView friendPhotoImgV=customView.findViewById(R.id.imgAddFriendPhoto);

        friendNameText.setText(friendName.get(position));
        try {
            Picasso.get().load(friendPhoto.get(position)).into(friendPhotoImgV);
        }catch (Exception e){
            e.printStackTrace();
        }
        return customView;
    }
}
