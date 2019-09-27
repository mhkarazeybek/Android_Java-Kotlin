package com.mhkarazeybek.uubmb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LocalDatabase extends AppCompatActivity {

    static ArrayList<String> chatsIdForLastMessages,messagesForLastMessages,typeForLastMessages;
    static ArrayList<Long> datesForLastMessages;
    static ArrayList<String> uidForFriend,nameForFriend,statusForFriend;
    static ArrayList<Bitmap> photoForFriend;
    static ArrayList<String>nameForInfo,statusForInfo,naptinCodeForInfo;
    static ArrayList<Bitmap> photoForInfo;
    static ArrayList<String>nameForBlockedPeople,statusForBlockedPeople,uidForBlockedPeople;
    static ArrayList<Bitmap> photoForBlockedPeople;
    static ArrayList<String> messageForChats,senderForChats;
    static ArrayList<Long> dateForChats;
    static Bitmap bitmapForFriend = null;
    static Bitmap bitmapForInfo=null;
    static Bitmap bitmapForBlockedPerson=null;
    static Context context;

    public LocalDatabase(Context context){
        chatsIdForLastMessages=new ArrayList<>();
        messagesForLastMessages=new ArrayList<>();
        typeForLastMessages=new ArrayList<>();
        datesForLastMessages=new ArrayList<>();

        uidForFriend=new ArrayList<>();
        nameForFriend=new ArrayList<>();
        statusForFriend=new ArrayList<>();
        photoForFriend=new ArrayList<>();

        nameForInfo=new ArrayList<>();
        statusForInfo=new ArrayList<>();
        naptinCodeForInfo=new ArrayList<>();
        photoForInfo=new ArrayList<>();

        nameForBlockedPeople=new ArrayList<>();
        statusForBlockedPeople=new ArrayList<>();
        uidForBlockedPeople=new ArrayList<>();
        photoForBlockedPeople=new ArrayList<>();

        messageForChats=new ArrayList<>();
        senderForChats=new ArrayList<>();
        dateForChats=new ArrayList<>();
        LocalDatabase.context=context;
    }


    static boolean getLastMessagesFromDB(){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin",Context.MODE_PRIVATE , null);
            database.execSQL("CREATE TABLE IF NOT EXISTS lastMessages (chatId VARCHAR, date LONG,message VARCHAR, type VARCHAR)");

            Cursor cursor = database.rawQuery("SELECT * FROM lastMessages", null);

            int chatIdIx = cursor.getColumnIndex("chatId");
            int dateIx = cursor.getColumnIndex("date");
            int messageIx=cursor.getColumnIndex("message");
            int typeIx=cursor.getColumnIndex("type");

            cursor.moveToFirst();

            while (cursor != null) {
                chatsIdForLastMessages.add(cursor.getString(chatIdIx));
                datesForLastMessages.add(cursor.getLong(dateIx));
                messagesForLastMessages.add(cursor.getString(messageIx));
                typeForLastMessages.add(cursor.getString(typeIx));
                cursor.moveToNext();

            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    static boolean setLastMessagesToDB(String chatId, Long date, String messsage, String type){
        if (controlLastMessage(chatId)){
            try {
            SQLiteDatabase database =context.openOrCreateDatabase("Naptin",Context.MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS lastMessages (chatId VARCHAR, date LONG,message VARCHAR, type VARCHAR)");
                String update="date = "+date+", message = '"+messsage+"', type = '"+type+"'";
                database.execSQL("UPDATE lastMessages SET "+update+" WHERE chatId = '"+chatId+"'");
                System.out.println("TRY 1");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("**********************************************");
                System.out.println(e.getMessage());
                return false;
            }

        }else {
            try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin",Context.MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS lastMessages (chatId VARCHAR, date LONG,message VARCHAR, type VARCHAR)");
                String sqlString = "INSERT INTO lastMessages (chatId, date, message, type) VALUES (?, ?, ?, ?)";
                SQLiteStatement statement = database.compileStatement(sqlString);
                if (chatId != "" && date != null && messsage!="" &&type!="") {
                    statement.bindString(1, chatId.trim());
                    statement.bindLong(2, date);
                    statement.bindString(3,messsage.trim());
                    statement.bindString(4,type.trim());
                    statement.execute();
                    System.out.println("TRY 2");
                    return true;

                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("CATCH 2");
                return false;
            }
        }
    }

    static boolean getFriendFromDB(){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin",Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS friends (uid VARCHAR,name VARCHAR,status VARCHAR, Photo BLOB)");

            Cursor cursor = database.rawQuery("SELECT * FROM friends", null);

            int uidIx = cursor.getColumnIndex("uid");
            int nameIx=cursor.getColumnIndex("name");
            int statusIx=cursor.getColumnIndex("status");
            int photoIx=cursor.getColumnIndex("Photo");

            cursor.moveToFirst();

            while (cursor != null) {
                uidForFriend.add(cursor.getString(uidIx));
                nameForFriend.add(cursor.getString(nameIx));
                byte[] byteArray = cursor.getBlob(photoIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                photoForFriend.add(image);
                statusForFriend.add(cursor.getString(statusIx));
                cursor.moveToNext();

            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean setFriendToDB(String uid, String name,String Photo,String status){

        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS friends (uid VARCHAR,name VARCHAR,status VARCHAR, Photo BLOB)");
            String sqlString = "INSERT INTO friends (uid, name,status,Photo) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sqlString);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmapForFriend=bitmap;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(Photo).into(target);

            bitmapForFriend.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] byteArray = outputStream.toByteArray();
            if (uid != "" &&name!=""&&status!=""&& Photo!="") {
                statement.bindString(1, uid.trim());
                statement.bindString(2,name.trim());
                statement.bindString(3,status.trim());
                statement.bindBlob(4, byteArray);
                statement.execute();
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    static boolean controlLastMessage(String uid){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS lastMessages (chatId VARCHAR, date LONG,message VARCHAR, type VARCHAR)");
            Cursor cursor = database.rawQuery("SELECT * FROM lastMessages WHERE chatId LIKE '%"+uid.trim()+"%'", null);
            int ingIndex = cursor.getColumnIndex("chatId");
            cursor.moveToFirst();
            if (cursor!=null){
                cursor.close();
                return true;
            }else {
                cursor.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean getInfoFromDB(){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS info (Photo BLOB, name VARCHAR,status VARCHAR, naptinCode VARCHAR)");
            Cursor cursor = database.rawQuery("SELECT * FROM info", null);

            int photoIx = cursor.getColumnIndex("Photo");
            int nameIx = cursor.getColumnIndex("name");
            int statusIx=cursor.getColumnIndex("status");
            int codeIx=cursor.getColumnIndex("naptinCode");

            cursor.moveToFirst();

            while (cursor != null) {
                byte[] byteArray = cursor.getBlob(photoIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                photoForInfo.add(image);
                nameForInfo.add(cursor.getString(nameIx));
                statusForInfo.add(cursor.getString(statusIx));
                naptinCodeForInfo.add(cursor.getString(codeIx));
                cursor.moveToNext();

            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean setInfoToDB(String Photo, String name, String status, String naptinCode){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS info (Photo BLOB, name VARCHAR,status VARCHAR, naptinCode VARCHAR)");
            String sqlString = "INSERT INTO info (Photo, name,status,naptinCode) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sqlString);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapForInfo =bitmap;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(Photo).into(target);

            bitmapForInfo.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] byteArray = outputStream.toByteArray();

            if (Photo != "" &&name!="" &&status!=""&&naptinCode!="")  {
                statement.bindBlob(1, byteArray);
                statement.bindString(2,name.trim());
                statement.bindString(3,status.trim());
                statement.bindString(4,naptinCode);
                statement.execute();
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    static boolean getBlockedPersonFromDB(){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS blockedPeople (uid VARCHAR, name VARCHAR,Photo BLOB,status VARCHAR)");

            Cursor cursor = database.rawQuery("SELECT * FROM blockedPeople", null);

            int uidIx = cursor.getColumnIndex("uid");
            int nameIx = cursor.getColumnIndex("name");
            int photoIx=cursor.getColumnIndex("Photo");
            int statusIx=cursor.getColumnIndex("status");

            cursor.moveToFirst();

            while (cursor != null) {
                uidForBlockedPeople.add(cursor.getString(uidIx));
                nameForBlockedPeople.add(cursor.getString(nameIx));
                statusForBlockedPeople.add(cursor.getString(statusIx));
                byte[] byteArray = cursor.getBlob(photoIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                photoForBlockedPeople.add(image);
                cursor.moveToNext();

            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean setBLockedPeopleToDB(String uid,String name,String Photo,String status){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Naptin", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS blockedPeople (uid VARCHAR, name VARCHAR,Photo BLOB,status VARCHAR)");
            String sqlString = "INSERT INTO blockedPeople (uid, name,Photo,status) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sqlString);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap selectedImage = null;

            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapForBlockedPerson=bitmap;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(Photo).into(target);

            selectedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] byteArray = outputStream.toByteArray();

            if (Photo != "" &&name!="" &&status!=""&&status!="")  {
                statement.bindString(1,uid.trim());
                statement.bindString(2,name.trim());
                statement.bindBlob(3, byteArray);
                statement.bindString(4,status.trim());
                statement.execute();
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean getChatsFromDB(String uid){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Sohbetler",Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS "+uid+" (date LONG,message VARCHAR, sender VARCHAR)");

            Cursor cursor = database.rawQuery("SELECT * FROM "+uid, null);

            int dateIx = cursor.getColumnIndex("date");
            int messageIx=cursor.getColumnIndex("message");
            int senderIx=cursor.getColumnIndex("sender");

            cursor.moveToFirst();

            while (cursor != null) {
                dateForChats.add(cursor.getLong(dateIx));
                messageForChats.add(cursor.getString(messageIx));
                senderForChats.add(cursor.getString(senderIx));
                cursor.moveToNext();

            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean setChatsToDB(String uid,Long date,String message,String sender){
        try {

            SQLiteDatabase database = context.openOrCreateDatabase("Sohbetler", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS "+uid+" (date LONG,message VARCHAR, sender VARCHAR)");
            String sqlString = "INSERT INTO "+uid+" (date, message, sender) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sqlString);
            if (uid != "" && date != null && message!="" &&sender!="") {
                statement.bindLong(1, date);
                statement.bindString(2,message.trim());
                statement.bindString(3,sender.trim());
                statement.execute();
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




}
