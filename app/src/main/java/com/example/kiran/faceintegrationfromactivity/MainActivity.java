package com.example.kiran.faceintegrationfromactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    CallbackManager callbackManager;
    Button share, details;
    ShareDialog shareDialog;
    LoginButton login;
    ProfilePictureView profile;
    Dialog details_dialog;
    TextView details_txt;
    String text,url,profilepicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*initialise the Facebook SDK*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
/*
* The CallbackManager manages the callbacks into the FacebookSdk
 * from an Activity's or Fragment's onActivityResult() method.
* */
        callbackManager = CallbackManager.Factory.create();
        login = (LoginButton) findViewById(R.id.login_button);
        profile = (ProfilePictureView) findViewById(R.id.picture);
        /*
        * Provides functionality to share content
        * shareDialog
        * */
        shareDialog = new ShareDialog(this);
        share = (Button) findViewById(R.id.share);
        details = (Button) findViewById(R.id.details);
        /*
        *  The permission to access Facebook profile and email is defined by using the method setReadPermissions()
         *  on the LoginButton object. Here we define as,
        * */
        login.setReadPermissions("public_profile","user_friends");
        share.setVisibility(View.INVISIBLE);
        details.setVisibility(View.INVISIBLE);
        /*
        *
        * */
        details_dialog = new Dialog(this);
        details_dialog.setContentView(R.layout.dialog_details);
        details_dialog.setTitle("Details");
        details_txt = (TextView) details_dialog.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                details_dialog.show();
            Intent intent= new Intent(getApplicationContext(),displayactivity.class);
                intent.putExtra("name",text);
                intent.putExtra("picturename",profilepicture);
                startActivity(intent);
            }
        });
        /*
        * Using the Access Token we can request the Facebook Graph API with the GraphRequest class
        *obtain an access token which provides temporary, secure access to Facebook APIs.
        * */
        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();
            share.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);

            }
        });

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                    share.setVisibility(View.VISIBLE);
                    details.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                /*try {*/
                    Log.d("jsondata", "" + json);
                /*} catch (JSONException e) {
                    e.printStackTrace();
                }*/
                try {
                    if (json != null) {
                        text = "<b>Name :</b> " + json.getString("name") +
                                "<br><br><b>Email :</b> " +
                                 "<br><br><b>Profile link :</b> " +
                                json.getString("link");
                        details_txt.setText(Html.fromHtml(text));
                        profilepicture=json.getString("id");
                        profile.setProfileId(json.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

