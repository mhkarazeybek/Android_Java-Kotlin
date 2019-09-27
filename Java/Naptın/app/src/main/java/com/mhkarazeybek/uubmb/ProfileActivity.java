package com.mhkarazeybek.uubmb;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ImageView ppImageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef,databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    Uri selected;
    private TextInputLayout uname,ustatus;
    private TextView user_uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uname = findViewById(R.id.user_name);
        ustatus = findViewById(R.id.user_status);
        user_uuid = findViewById(R.id.user_uuid_code);
        setTitle("Profil Ayarları");


        ppImageView=findViewById(R.id.profile_image);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        getting();

        getUserPhoto();

    }
    public void getting(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                uname.getEditText().setText(name);
                ustatus.getEditText().setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid()).child("naptinCode");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uuid = dataSnapshot.getValue().toString();

                user_uuid.setText(uuid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void save(View view){
        upload();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Naptin").child(mAuth.getUid());
        String usname = uname.getEditText().getText().toString();
        String ustat = ustatus.getEditText().getText().toString();

        databaseReference.child("name").setValue(usname).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Kaydedildi!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),ChatsActivity.class));
            }
        });
        databaseReference.child("status").setValue(ustat);
        databaseReference.child("Photo").setValue(downloadURL);

    }
    String downloadURL="https://firebasestorage.googleapis.com/v0/b/uubmb-50237.appspot.com/o/naptinLogo.png?alt=media&token=3663110b-33df-40b7-8814-a7e6097b6a23";

    public void getUserPhoto(){
        try{
            final DatabaseReference newRef=myRef.child("Naptin").child(mAuth.getUid()).child("Photo");
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(ppImageView);
                    downloadURL=dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void upload() {
        if (selected!=null){
            UUID uuidImage=UUID.randomUUID();
            String imageName=mAuth.getUid()+"/"+uuidImage+".jpg";
            final StorageReference storageReference=mStorageRef.child(imageName);
            storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadURL = task.getResult().toString();
                            myRef.child("Naptin").child(mAuth.getUid()).child("Photo").setValue(downloadURL);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                }
            });
        }else{
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
                /*Intent intent=new Intent(getApplicationContext(),FriendList.class);
                startActivity(intent);*/
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void chooseImage(View view){
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selected);
                ppImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChatsActivity.class));
    }*/
}
