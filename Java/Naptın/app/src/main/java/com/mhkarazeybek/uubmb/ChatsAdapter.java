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

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatsAdapter extends ArrayAdapter<String> {

        private final ArrayList<String> friendPhoto,lastMessage;
        private final ArrayList<String> friendName;
        private final ArrayList<Long> messageDates;
        private final Activity context;
        private final FirebaseAuth mAuth;

    public ChatsAdapter(ArrayList<String> friendPhoto, ArrayList<String> friendName,ArrayList<Long> dates,ArrayList<String> lastMessage, Activity context) {
        super(context,R.layout.custom_chats_view, friendName);
        this.friendPhoto = friendPhoto;
        this.friendName = friendName;
        this.messageDates = dates;
        this.lastMessage = lastMessage;
        this.context = context;
        mAuth=FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_chats_view,null,true);

        final TextView friendNameText=customView.findViewById(R.id.txtFriendName);
        final ImageView friendPhotoImgV=customView.findViewById(R.id.imgFriendPhoto);
        final TextView lastMessageText=customView.findViewById(R.id.txtLastMessage);
        final TextView lastMessageDateText=customView.findViewById(R.id.txtLastMessageDate);
        final TextView lastMessageDayText=customView.findViewById(R.id.txtLastMessageDay);
        friendNameText.setText(friendName.get(position));


        try {
            Picasso.get().load(friendPhoto.get(position)).into(friendPhotoImgV);
        }catch (Exception e){
            e.printStackTrace();
        }

        Date date=new Date();
        Date dateNow=new Date();
        Date date1=new Date();
        Date date2=new Date();
        //SimpleDateFormat bicimDeneme=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        // "dd-M-yyyy hh:mm:ss" 17-11-2018 04:02:12
        SimpleDateFormat bicim=new SimpleDateFormat( "HH:mm");
        SimpleDateFormat bicimDate=new SimpleDateFormat("dd-M-yyyy");
        SimpleDateFormat bicimDay=new SimpleDateFormat("dd");

        try{
            long d1=messageDates.get(position)%86400000;
            date1.setTime(messageDates.get(position)-d1);
            long dN=dateNow.getTime()%86400000;
            dateNow.setTime(dateNow.getTime()-dN);

            if (position>=1){
                long d2=messageDates.get(position-1)%86400000;
                date2.setTime(messageDates.get(position-1)-d2);

                    if ((String.valueOf(bicimDate.format(dateNow.getTime())).equals(String.valueOf(bicimDate.format(date1.getTime()))))) {
                        lastMessageDayText.setText("Bugün");
                    } else if (dateNow.getTime()-date1.getTime() == 86400000)  {
                        lastMessageDayText.setText("Dün");
                    } else {
                        date.setTime(messageDates.get(position));
                        lastMessageDayText.setText(String.valueOf(bicimDate.format(date.getTime())));
                    }

            }else if(position==0){
                if ((Integer.parseInt(bicimDay.format(dateNow))-Integer.parseInt(bicimDay.format(date1.getTime()))) == 0) {
                    lastMessageDayText.setText("Bugün");
                } else if (dateNow.getTime()-date1.getTime() == 86400000)  {
                    lastMessageDayText.setText("Dün");
                } else {
                    date.setTime(messageDates.get(position));
                    lastMessageDayText.setText(String.valueOf(bicimDate.format(date.getTime())));
                }
            }
            date.setTime(messageDates.get(position));
            lastMessageDateText.setText(String.valueOf(bicim.format(date.getTime())));
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            lastMessageText.setText(lastMessage.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }

        return customView;
    }
}
