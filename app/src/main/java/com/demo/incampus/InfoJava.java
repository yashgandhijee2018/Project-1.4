package com.demo.incampus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoJava extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    EditText name_et;
    Button signout_ggl;
    //String serverClientId="812077473460-pvbqdjirafrelcard0ni1ao02r3dde8r.apps.googleusercontent.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if the system api is below marshmallow, set status bar to default black
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_java);

        //DECLARATIONS:
        name_et=findViewById(R.id.name_et);
        signout_ggl=findViewById(R.id.signout_ggl);

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

            name_et.setText(personName);

            //API POST REGISTER
            Call<ResponseBody> register=RetrofitClient.getInstance().getApi().register(personGivenName,personName,personId);
            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s=response.body().string();
                        Toast.makeText(InfoJava.this, s, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(InfoJava.this,"User already registered!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(InfoJava.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
            /*
            //API POST LOGIN USER
            Call<ResponseBody> login=RetrofitClient.getInstance().getApi().login(personGivenName,personId);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s=response.body().string();
                        Toast.makeText(InfoJava.this, s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(InfoJava.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
             */

        }
        //signout button google:
        signout_ggl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id.signout_ggl:
                        signOut();
                        break;
                    // ...
                }
            }
        });

        //TODO) OTP PART REMOVE THIS CODE FROM HERE
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();

        /*
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", "812077473460-pvbqdjirafrelcard0ni1ao02r3dde8r.apps.googleusercontent.com")
                .add("client_secret", "jZhij_V5dbNDKye2l1vmd-bw" )
                .add("redirect_uri","http://localhost:4500/api/v1/google/callback")
                .add("code", "4/xwHdaSITLLZrNZ-49vbLBWZmEq-L9yc4a2NgPjmcDuc7h1icsAUOLb44aNEaqQZZeYqBJriaY0URNg3jRqxfYw0")
                .build();
        final Request request = new Request.Builder()
                .url("https://accounts.google.com/o/oauth2/token")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.e("Error occured:", e.toString());
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String message = jsonObject.toString(5);
                    Log.i("excess token:", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         */
    }

    /*_______________________________________START_GOOGLE____________________________________________*/
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        finish();
                        Intent i=new Intent(InfoJava.this,MainActivityJava.class);
                        startActivity(i);
                    }
                });
    }
    /*__________________________________________END_GOOGLE______________________________________________*/
    public void  go_to_addpfp_activity_function(View view)
    {
        Intent i=new Intent(this,AddPfP.class);
        startActivity(i);
    }

}
