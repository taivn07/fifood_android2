package paditech.com.fifood_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import GPSTracker.CheckConnectNetwork;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import Constant.Constant;
import Object.User;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements Constant {
    private View btnLoginFb, btnJoinNow;
    private CallbackManager callbackManager;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    public static User user;
    public static String lang = "vi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        user = new User();
        preferences = this.getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
        user.setUserID(preferences.getString(ID, null));
        user.setToken(preferences.getString(TOKEN, null));
        if (user.getUserID() != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {

        callbackManager = CallbackManager.Factory.create();

        btnLoginFb = findViewById(R.id.btnLoginFb);
        btnJoinNow = findViewById(R.id.tvJoinNow);

        btnJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = null;
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setBtnLoginFbClicked();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setBtnLoginFbClicked() {
        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnectNetwork.isNetworkOnline(LoginActivity.this)) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_photos", "public_profile"));
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AccessToken accessToken = AccessToken.getCurrentAccessToken();
                            String fbToken = accessToken.getToken().toString();
                            preferences.edit().putString(FB_TOKEN, fbToken).commit();
                            Log.e("SUCCESS", fbToken + "\n" + loginResult.getAccessToken().getToken());
                            getUserInfo(LoginActivity.lang, fbToken, loginResult);
                        }

                        @Override
                        public void onCancel() {
                            Log.e("SUCCESS", "cancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.e("SUCCESS", error + "");
                        }
                    });
                } else {
                    CheckConnectNetwork.showNotifyNetwork(LoginActivity.this);
                }
            }
        });
    }

    private void getUserInfo(String lang, String token, final LoginResult loginResult) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(FB_TOKEN, token);
        aClient.post(BASE_URL + AUTH, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("POST AUTH", "FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("GET INFO", responseString + "");
                    JSONObject response = new JSONObject(responseString).getJSONObject(RESPONSE);
                    if (responseString.contains("\"result\":")) {
                        registerByFacebook(loginResult);
                    } else {
                        JSONObject u = response.getJSONObject(USER);
                        preferences.edit().putString(TOKEN, u.getString(TOKEN)).commit();
                        preferences.edit().putString(ID, u.getString(ID)).commit();


                        user.setEmail(u.getString(EMAIL));
                        user.setUserID(u.getString(ID));
                        user.setProfile_image(u.getString(PROFILE_IMAGE));
                        user.setNickname(u.getString(NICKNAME));
                        user.setToken(u.getString(TOKEN));
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registerByFacebook(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        String email = object.optString("email");
                        String id = object.optString("id");
                        String name = object.optString("name");
                        Log.e("FB", response.toString() + "");

                        User user = new User();
                        user.setEmail(email);
                        user.setFb_id(id + "11");
                        user.setNickname(name);
                        user.setProfile_image("http://graph.facebook.com/" + id + "/picture?type=large");
                        Date cDate = new Date();
                        String fDate = new SimpleDateFormat("yyyy-MM-dd h:m:s").format(cDate);
                        Log.e("DATE", fDate);
                        user.setExpires(fDate);

                        registerAccount(user, "vi");

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registerAccount(User u, String lang) {
        AsyncHttpClient aClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(EMAIL, u.getEmail());
        params.put(FB_ID, u.getFb_id());
        params.put(LANG, lang);
        params.put(EXPIRES, u.getExpires());
        params.put(NICKNAME, u.getNickname());
        params.put(PROFILE_IMAGE, u.getProfile_image());

        aClient.post(BASE_URL + REGISTER, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("REGISTER", "FAIL");
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    String res = responseString.substring(responseString.indexOf("{"));
                    Log.e("REGISTER", res + "");
                    if(res.contains("\"result\":false")){
                        Toast.makeText(LoginActivity.this, "Facebook đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONObject response = new JSONObject(res).getJSONObject(RESPONSE).getJSONObject(USER);
                        preferences.edit().putString(TOKEN, response.getString(TOKEN)).commit();
                        preferences.edit().putString(ID, response.getString(ID)).commit();

                        user.setEmail(response.getString(EMAIL));
                        user.setUserID(response.getString(ID));
                        user.setProfile_image(response.getString(PROFILE_IMAGE));
                        user.setNickname(response.getString(NICKNAME));
                        user.setToken(response.getString(TOKEN));

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        });
    }
}

