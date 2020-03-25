package com.demo.incampus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPJava extends AppCompatActivity {

    String phoneNumber="";
    String OTP="";
    EditText OTP_text;
    String serverAccessToken="";
    String sessionID="";
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if the system api is below marshmallow, set status bar to default black
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_java);

        Intent i = getIntent();
        phoneNumber= i.getExtras().getString("phoneNumber");

        OTP_text=findViewById(R.id.OTP);
        Log.i("Phone",phoneNumber);
        API_POST_receive_OTP(phoneNumber);//RECEIVE SessionID and OTP

        /*GOOGLE*/
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                //.requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String personExcessToken=acct.getIdToken();

            API_POST_login_user(personGivenName,personId);//LOGIN USER API CALL
        }

        /*GOOGLE END*/
    }


    public void go_to_InfoJava_activity_function(View view)
    {
        Intent i=new Intent(this,InfoJava.class);
        startActivity(i);
    }

    public void verify_OTP_function(View view)
    {
        OTP=OTP_text.getText().toString();
        //Todo():call API_POST_verify_OTP
        //didn't called yet because OTP received is very late which fails verification and exception occurs
        go_to_InfoJava_activity_function(view);
    }
    public void API_POST_receive_OTP(String phoneNumber)
    {
        //API CALL RECEIVE OTP
        Call<ResponseBody> otpReceive=RetrofitClient.getInstance().getApi().otpReceive(phoneNumber);
        otpReceive.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    Log.i("response",s);

                    JSONObject jsonObject=new JSONObject();
                    sessionID=jsonObject.getString("Details");
                    Log.i("sessionID",sessionID);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(OTPJava.this, "Exception", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OTPJava.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void API_POST_verify_OTP(String OTP,String sessionID)
    {
        //API CALL VERIFY OTP
        Call<ResponseBody> verifyotp=RetrofitClient.getInstance().getApi().verifyotp(OTP,sessionID);
        verifyotp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   String s = response.body().string();
                    Log.i("verification",s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OTPJava.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void API_POST_login_user(String personGivenName,String personId)
    {
        //API POST LOGIN USER
        Call<ResponseBody> login=RetrofitClient.getInstance().getApi().login(personGivenName,personId);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s=response.body().string();

                    JSONObject jsonObject=new JSONObject(s);
                    serverAccessToken=jsonObject.getString("accessToken");
                    Log.i("access Token Server:",s);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OTPJava.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void API_POST_register_user(String personGivenName,String personName,String personId)
    {
        //API POST REGISTER
        Call<ResponseBody> register=RetrofitClient.getInstance().getApi().register(personGivenName,personName,personId);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s=response.body().string();
                    Toast.makeText( OTPJava.this, s, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(OTPJava.this,"User already registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OTPJava.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
