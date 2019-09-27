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

public class WantsAdapter extends ArrayAdapter<String>{



    private final ArrayList<String> wantPhoto;
    private final ArrayList<String> wantName;
    private final ArrayList<String> wantUid;
    private final Activity context;


    public WantsAdapter(ArrayList<String> wantName, ArrayList<String> wantPhoto,ArrayList<String> wantUid, Activity context) {
        super(context,R.layout.custom_wants_view, wantName);
        this.wantPhoto = wantPhoto;
        this.wantName = wantName;
        this.wantUid=wantUid;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_wants_view,null,true);

        final TextView wantNameText=customView.findViewById(R.id.txtWantName);
        final ImageView wantPhotoImgView=customView.findViewById(R.id.imgWantPhoto);
        final ImageView wantAcceptBtn=customView.findViewById(R.id.btnWantTick);
        final ImageView wantCancelBtn=customView.findViewById(R.id.btnWantCancel);
        String dizi[]=new String[2];
        dizi[0]=wantName.get(position);
        dizi[1]=wantUid.get(position);
        wantNameText.setText(wantName.get(position));
        wantAcceptBtn.setTag(dizi);
        wantCancelBtn.setTag(dizi);
        try {
            Picasso.get().load(wantPhoto.get(position)).into(wantPhotoImgView);
        }catch (Exception e){
            e.printStackTrace();
        }
        return customView;
    }

}
