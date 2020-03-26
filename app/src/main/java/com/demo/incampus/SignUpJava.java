 package com.demo.incampus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.incampus.Model.Register;
import com.demo.incampus.Model.SignUp;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
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
import retrofit2.http.FormUrlEncoded;

public class SignUpJava extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButtonGoogle;

    int RC_SIGN_IN=0,RC_GET_AUTH_CODE=0;

    EditText email,password,username;

    final SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

    String serverClientId="812077473460-pvbqdjirafrelcard0ni1ao02r3dde8r.apps.googleusercontent.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if the system api is below marshmallow, set status bar to default black
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_java);

        email=findViewById(R.id.email);
        password=findViewById(R.id.pwd);
        username=findViewById(R.id.username);


        /*_______________________________________START_GOOGLE____________________________________________*/
        signInButtonGoogle=findViewById(R.id.login_ggl);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.login_ggl:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        /*__________________________________________END_GOOGLE______________________________________________*/



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivityJava.class);
        startActivity(i);
    }

    public void go_to_signin_function(View view) {
        Intent i = new Intent(this, SignInJava.class);
        startActivity(i);
    }
    public void go_to_OTP_activity_function(View view) {

        if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||username.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Fields can't be Empty", Toast.LENGTH_SHORT).show();
        }
        else {
            API_POST_register_user(email.getText().toString(),username.getText().toString(),password.getText().toString());
        }
    }

    /*_______________________________________START_GOOGLE____________________________________________*/
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
     }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent i=new Intent(this,PhoneNumberActivity.class/*AutthenticateTempActivity.class*/);
            startActivity(i);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error:", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void getAuthCode() {
        // Start the retrieval process for a server auth code.  If requested, ask for a refresh
        // token.  Otherwise, only get an access token if a refresh token has been previously
        // retrieved.  Getting a new access token for an existing grant does not require
        // user consent.
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent i=new Intent(this,OTP.class);
        startActivity(i);
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        if(requestCode==RC_GET_AUTH_CODE)
        {
            SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("Success: ", "onActivityResult:GET_AUTH_CODE:success:" + result.getStatus().isSuccess());

            if (result.isSuccess()) {
                // [START get_auth_code]
                GoogleSignInAccount acct = result.getSignInAccount();
                String authCode = acct.getServerAuthCode();
                String idTokenString = acct.getIdToken();

                // Show signed-in UI.
                Log.i("Auth: ",authCode);
                //STORING IN SHARED PREFERENCES GOOGLE AUTH CODE
                editor.putString("Auth", authCode );
                editor.commit();
                // TODO(user): send code to server and exchange for access/refresh/ID tokens.
                // [END get_auth_code]
            }
        }
    }
    /*__________________________________________END_GOOGLE______________________________________________*/

    public void API_POST_register_user(String personEmail,String personGivenName,String personPassword)
    {
        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        //API POST REGISTER
        retrofit2.Call<Register> register=RetrofitClient.getInstance().getApi().register(personEmail,personGivenName,personPassword);
        register.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(retrofit2.Call<Register> call, Response<Register> response) {
                try {

                    /*Register s=response.body();
                    editor.putString("JWT", s.getAccessToken());
                    editor.commit();
                     */
                    Intent i=new Intent(SignUpJava.this,PhoneNumberActivity.class);
                    startActivity(i);


                    Log.i("response","Got response from user");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpJava.this,"User already registered!", Toast.LENGTH_SHORT).show();
                    Log.i("No response","exception");
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(SignUpJava.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Info","No response for register user post");
            }

        });
    }

    public void API_POST_google_auth_response()
    {

    }
}
