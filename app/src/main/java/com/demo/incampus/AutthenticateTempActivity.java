package com.demo.incampus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class AutthenticateTempActivity extends AppCompatActivity {

    EditText phone_text;
    EditText OTP_text;
    String serverAccessToken="";
    public String phoneNumber="";
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        phone_text=findViewById(R.id.phone);
        OTP_text=findViewById(R.id.OTP);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autthenticate_temp);


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

            /*
            //API POST REGISTER
            Call<ResponseBody> register=RetrofitClient.getInstance().getApi().register(personGivenName,personName,personId);
            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s=response.body().string();
                        Toast.makeText(AutthenticateTempActivity.this, s, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AutthenticateTempActivity.this,"User already registered!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AutthenticateTempActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

             */

            //API POST LOGIN USER
            Call<ResponseBody> login=RetrofitClient.getInstance().getApi().login(personGivenName,personId);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s=response.body().string();
                        Toast.makeText(AutthenticateTempActivity.this, s, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject=new JSONObject(s);
                        serverAccessToken=jsonObject.getString("accessToken");
                        Log.i("access Token Server:",s);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AutthenticateTempActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void receive_OTP_function(View view)
    {
        try {
            phoneNumber = phone_text.getText().toString();
        }
        catch (Exception e)
        {

        }
        /*
        //API CALL RECEIVE OTP
        Call<ResponseBody> otpReceive=RetrofitClient.getInstance().getApi().otpReceive(phoneNumber);
        otpReceive.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   String s = new String();
                   s=response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AutthenticateTempActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
         */
    }

    public void go_to_InfoJava_activity_function(View view)
    {
        Intent i=new Intent(this,InfoJava.class);
        startActivity(i);
    }
}
