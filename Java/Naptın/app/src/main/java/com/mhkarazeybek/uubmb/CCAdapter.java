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

public class CCAdapter extends ArrayAdapter {

    private final ArrayList<String> friendPhoto;
    private final ArrayList<String> friendName;
    private final ArrayList<String> friendStatus;
    private final Activity context;

    public CCAdapter(ArrayList<String> friendPhoto, ArrayList<String> friendName,ArrayList<String> friendStatus, Activity context) {
        super(context,R.layout.custom_cc_view, friendName);
        this.friendPhoto = friendPhoto;
        this.friendName = friendName;
        this.friendStatus=friendStatus;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_cc_view,null,true);

        final TextView friendNameText=customView.findViewById(R.id.txtCCName);
        final TextView friendStatusText=customView.findViewById(R.id.txtCCStatus);
        final ImageView friendPhotoImgV=customView.findViewById(R.id.imgCCPhoto);


        try {
            friendNameText.setText(friendName.get(position));
            Picasso.get().load(friendPhoto.get(position)).into(friendPhotoImgV);
            friendStatusText.setText(friendStatus.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }

        return customView;
    }
}