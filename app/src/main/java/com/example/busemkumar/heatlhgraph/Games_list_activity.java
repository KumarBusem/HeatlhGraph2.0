package com.example.busemkumar.heatlhgraph;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.Toast;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.firebase.auth.FirebaseAuth;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

public class Games_list_activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    FirebaseAuth auth;
    private Games_adapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private String object="405310";
    public static int flag = 0;
    private String murl;
    String[] keys = {"307690", "252950" , "225540", "435150", "399820", "283640", "271590", "387290", "400630", "378540"};
    List<Games_Data> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list_activity);
        getSupportActionBar().setTitle("Games List");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        auth=FirebaseAuth.getInstance();
        //Make call to AsyncTask
        flag=0;
        int i;
        for (i = 0; i < 10; i++) {
            AsyncFetch a = new AsyncFetch();
            a.execute();
        }
       // Toast.makeText(this, "completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
    private class AsyncFetch extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(Games_list_activity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            object = keys[flag];
            murl = "https://store.steampowered.com/api/appdetails/?appids=" + object;
            String url = "https://steamdb.info/api/GetGraph/?type=concurrent_week&appid=" + object;
            Https_Handler sh = new Https_Handler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(murl);
            String playcount = sh.makeServiceCall(url);
            Games_Data fishData = new Games_Data();
            if (playcount != null) {
                try {
                    JSONObject jsonObj = new JSONObject(playcount);
                    ;
                    JSONObject c = jsonObj.getJSONObject("data");
                    JSONArray contacts = c.getJSONArray("values");
                    fishData.players_count = contacts.length() + "";
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                } catch (Throwable tx) {

                    Toast.makeText(Games_list_activity.this, jsonStr + "", Toast.LENGTH_SHORT).show();
                }
            }

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject b = jsonObj.getJSONObject(object);
                    JSONObject c = b.getJSONObject("data");
                    fishData.Game_id = object;
                    fishData.GameName = c.getString("name");
                    ;
                    fishData.Game_url = c.getString("header_image");
                    data.add(fishData);

                } catch (final JSONException e) {
                    Log.e("sd", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("AG", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            flag++;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();

            pdLoading.dismiss();

            // Setup and Handover data to recyclerview
            mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
            mAdapter = new Games_adapter(Games_list_activity.this, data);
            mRVFishPrice.setAdapter(mAdapter);
            mRVFishPrice.setLayoutManager(new LinearLayoutManager(Games_list_activity.this));

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Logout) {

            auth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Toast.makeText(Games_list_activity.this, "SignOut...", Toast.LENGTH_SHORT).show();
                        }
                    });

            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
            Intent a = new Intent(this,Login_screen_activity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        return true;

    }
}



