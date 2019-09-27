package com.mhkarazeybek.uubmb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesAdapter extends ArrayAdapter {
    private final ArrayList<String> messages,senders;
    private final ArrayList<Long> dates;
    private final Activity context;
    private FirebaseAuth mAuth;

    public MessagesAdapter(ArrayList<String> messages,ArrayList<Long> dates,ArrayList<String> senders, Activity context) {
        super(context,R.layout.custom_messages_view, messages);
        this.messages = messages;
        this.senders=senders;
        this.dates=dates;
        this.context = context;
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        final LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_messages_view,null,true);
        final TextView messageOText=customView.findViewById(R.id.txtMessageO);
        final TextView messageIText=customView.findViewById(R.id.txtMessageI);
        final TextView dateOText=customView.findViewById(R.id.txtDateO);
        final TextView dateIText=customView.findViewById(R.id.txtDateI);
        final TextView dayText=customView.findViewById(R.id.txtDay);

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
            long d1=dates.get(position)%86400000;
            date1.setTime(dates.get(position)-d1);
            long dN=dateNow.getTime()%86400000;
            dateNow.setTime(dateNow.getTime()-dN);
            
            if (position>=1){
                long d2=dates.get(position-1)%86400000;
                date2.setTime(dates.get(position-1)-d2);

                if (!(String.valueOf(bicimDate.format(date1.getTime())).equals(String.valueOf(bicimDate.format(date2.getTime()))))) {
                    if ((String.valueOf(bicimDate.format(dateNow.getTime())).equals(String.valueOf(bicimDate.format(date1.getTime()))))) {
                        dayText.setText("Bugün");
                    } else if (dateNow.getTime()-date1.getTime() == 86400000)  {
                        dayText.setText("Dün");
                    } else {
                        date.setTime(dates.get(position));
                        dayText.setText(String.valueOf(bicimDate.format(date.getTime())));
                    }
                }
            }else if(position==0){
                if ((Integer.parseInt(bicimDay.format(dateNow))-Integer.parseInt(bicimDay.format(date1.getTime()))) == 0) {
                    dayText.setText("Bugün");
                } else if (dateNow.getTime()-date1.getTime() == 86400000)  {
                    dayText.setText("Dün");
                } else {
                    date.setTime(dates.get(position));
                    dayText.setText(String.valueOf(bicimDate.format(date.getTime())));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (senders.get(position).equals(mAuth.getUid())){
                messageOText.setText(messages.get(position));
                date.setTime(dates.get(position));
                dateOText.setText(String.valueOf(bicim.format(date.getTime())));
            }
            else{
                messageIText.setText(messages.get(position));
                date.setTime(dates.get(position));
                dateIText.setText(String.valueOf(bicim.format(date.getTime())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return customView;
    }
}
