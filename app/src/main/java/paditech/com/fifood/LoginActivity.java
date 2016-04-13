package paditech.com.fifood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import Constant.Constant;

public class LoginActivity extends AppCompatActivity implements Constant {

    private View btnLoginFb;
    private LoginButton btnFb;
    private CallbackManager callbackManager;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String accEmail = preferences.getString(EMAIL,null);


        setContentView(R.layout.activity_login);

        if (accEmail!= null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }

        init();


    }

    private void init() {

        callbackManager = CallbackManager.Factory.create();

        btnLoginFb = findViewById(R.id.btnLoginFb);
        btnFb = (LoginButton) findViewById(R.id.login_button);

        setBtnLoginFbClicked();
        registerCallback();


    }

    private void registerCallback() {
        btnFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login sucess", Toast.LENGTH_SHORT).show();

                Log.e("STATUS", "sucess");
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancel", Toast.LENGTH_SHORT).show();
                Log.e("STATUS", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
                Log.e("STATUS", "err");

            }
        });
    }

    private void setBtnLoginFbClicked() {
        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFb.callOnClick();

            }
        });
    }
}

