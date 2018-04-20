package com.example.busemkumar.heatlhgraph;

        import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_activity extends AppCompatActivity {
    EditText email;
    TextInputLayout password;
    private FirebaseAuth firebase;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        firebase = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
    }
    public void onBackPressed() {
        startActivity(new Intent(this,Login_screen_activity.class));
    }

    public void regtofire(View view) {
        String pass = password.getEditText().getText().toString().trim();
        String mail = email.getText().toString().trim();
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(mail)) {
            Toast.makeText(this, "Please Fill ALL Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (pass.length()<=8)
        {
            password.setError("Please Enter Atleast 8 Charcters");
            return;
        }else {
            progressDialog.setMessage("Creating User.....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            firebase.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (!task.isSuccessful()) {
                        Toast.makeText(Register_activity.this, "Account Not Created", Toast.LENGTH_SHORT).show();
                    } else {
                        sendVerificationEmail();

                    }
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
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Register_activity.this, Login_screen_activity.class));
                            finish();
                            Toast.makeText(Register_activity.this, "Click on the verification link send to your mail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
