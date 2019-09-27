package com.mhkarazeybek.uubmb;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PopUpActivity extends Activity {

    FirebaseAuth mAuth;
    TextView forgotEmailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        mAuth=FirebaseAuth.getInstance();
        forgotEmailText=findViewById(R.id.txtForgotPasswdEmail);
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
    }

    public void pupUpClose(View view) {
        finish();
    }

    public void sendEmail(View view) {
        mAuth.sendPasswordResetEmail(forgotEmailText.getText().toString().trim());
        Toast.makeText(this, "Email sended", Toast.LENGTH_SHORT).show();
        finish();
    }
}
