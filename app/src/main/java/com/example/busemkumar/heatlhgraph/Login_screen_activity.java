package com.example.busemkumar.heatlhgraph;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_screen_activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    String personName,personPhotoUrl,email;
    FirebaseAuth firebaseUser;
    FirebaseUser fireuser;
    ProgressDialog progressDialog;
    EditText ed;
    TextInputLayout textInputLayout;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().hide();
        btnSignIn = (SignInButton)findViewById(R.id.btn_sign_in);
        ed=(EditText)findViewById(R.id.eemail);
        textInputLayout=(TextInputLayout)findViewById(R.id.epass);
        firebaseUser=FirebaseAuth.getInstance();
        fireuser=firebaseUser.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Exit the App?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Splash_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }).setNegativeButton("No", null).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (firebaseUser.getCurrentUser()!=null)
            checkIfEmailVerified();
        else if (opr.isDone()) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Games_list_activity.class));
        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Toast.makeText(Login_screen_activity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Games_list_activity.class));
        }
        else
        {
            Toast.makeText(this, "Please Verify Your Mail", Toast.LENGTH_SHORT).show();
            sendVerificationEmail();
            FirebaseAuth.getInstance().signOut();
        }
    }
    public void Login_for_games(View view) {
        progressDialog.setMessage("Loging You In.....");
        String name=ed.getText().toString().trim();
        String pass=textInputLayout.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please Enter mail and password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressDialog.show();
            progressDialog.setCancelable(false);
            firebaseUser.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login_screen_activity.this, "Sigin Falied", Toast.LENGTH_SHORT).show();

                            } else {
                                checkIfEmailVerified();
                            }
                            // ...
                        }
                    });
        }

    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_screen_activity.this, "Sended to your mailid", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Toast.makeText(this, "Login SucessFull", Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acct = result.getSignInAccount();;
            personName = acct.getDisplayName();
            personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            startActivity(new Intent(this,Games_list_activity.class));
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
    public void createAcoount(View view) {
        startActivity(new Intent(this,Register_activity.class));
    }
    public void nav_contact(View view) {
        startActivity(new Intent(this,Contact_screen_activity.class));
    }
}