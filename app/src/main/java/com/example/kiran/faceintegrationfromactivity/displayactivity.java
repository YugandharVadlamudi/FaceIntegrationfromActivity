package com.example.kiran.faceintegrationfromactivity;

import android.content.Intent;
import android.media.FaceDetector;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

public class displayactivity extends AppCompatActivity {
TextView textView;

ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        CallbackManager callbackManager=CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayactivity);
        textView=(TextView)findViewById(R.id.tv_displayfromjson);
        profilePictureView=(ProfilePictureView)findViewById(R.id.picture);
        Intent intent=getIntent();
        textView.setText(Html.fromHtml(intent.getStringExtra("name")));
        profilePictureView.setProfileId(intent.getStringExtra("picturename"));
    }
}
