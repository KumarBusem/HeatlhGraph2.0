package com.example.busemkumar.heatlhgraph;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Game_Information_activity extends AppCompatActivity {
    Bundle bd;
    private String Image_url;
    ImageView sc1,sc2,sc3,sc4,sc5,sc6,sc7,sc8,sc9,sc10;
    TextView tv_pc_req,req_text,tv_about_game,tv_descprtion,support,published, name_textview, age_textview, con_support_textview,tv_short_description,tv_support_lan,tv_mac_req,tv_linux_req;
    ImageView iv;
    LinearLayout req_layoout;
    String kk,name,req_age,controller_support,short_description,support_languages,header_image,webiste,count,pc_req,mac_req,linux_req,release_date,support_info,about_game,descprtion;
    boolean cost;
    String[] screenshots_array =new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__information_activity);
        bd=getIntent().getExtras();

        Image_url = "https://store.steampowered.com/api/appdetails/?appids="+bd.getString("key");
        name_textview =(TextView)findViewById(R.id.name);
        age_textview =(TextView)findViewById(R.id.required_age);
        con_support_textview =(TextView)findViewById(R.id.controller_support);
        tv_short_description=(TextView)findViewById(R.id.short_description);
        tv_support_lan=(TextView)findViewById(R.id.support_language);
        tv_mac_req=(TextView)findViewById(R.id.tv_mac_req);
        tv_linux_req=(TextView)findViewById(R.id.tv_linux_req);
        tv_pc_req=(TextView)findViewById(R.id.tv_pc_req);
        tv_about_game=(TextView)findViewById(R.id.tv_about_game);
        req_text=(TextView)findViewById(R.id.req_text);
        tv_descprtion=(TextView)findViewById(R.id.tv_descprtion);
        req_layoout=(LinearLayout)findViewById(R.id.req_layout);
        support=(TextView)findViewById(R.id.support);
        published=(TextView)findViewById(R.id.published);

        sc1=(ImageView)findViewById(R.id.screenshot1);
        sc2=(ImageView)findViewById(R.id.screenshot2);
        sc3=(ImageView)findViewById(R.id.screenshot3);
        sc4=(ImageView)findViewById(R.id.screenshot4);
        sc5=(ImageView)findViewById(R.id.screenshot5);
        sc6=(ImageView)findViewById(R.id.screenshot6);
        sc7=(ImageView)findViewById(R.id.screenshot7);
        sc8=(ImageView)findViewById(R.id.screenshot8);
        sc9=(ImageView)findViewById(R.id.screenshot9);
        sc10=(ImageView)findViewById(R.id.screenshot10);
        iv=(ImageView)findViewById(R.id.header_image);
        AsyncFetch a=new AsyncFetch();
        a.execute();
    }
    private class AsyncFetch extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(Game_Information_activity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Https_Handler sh = new Https_Handler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Image_url);
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject b=jsonObj.getJSONObject(""+bd.getString("key"));
                JSONObject c=b.getJSONObject("data");
                JSONArray screenshots=c.getJSONArray("screenshots");
                for(int i=0;i<10;i++){
                    JSONObject json_data = screenshots.getJSONObject(i);
                    screenshots_array[i]=json_data.getString("path_thumbnail");
                }
                name=c.getString("name");
                req_age=c.getString("required_age");
                controller_support=c.getString("controller_support");
                short_description=c.getString("short_description");
                cost=c.getBoolean("is_free");
                support_languages=c.getString("supported_languages");
                header_image=c.getString("header_image");
                webiste=c.getString("website");
                about_game=c.getString("about_the_game");
                release_date =c.getJSONObject("release_date").getString("date");
                support_info=c.getJSONObject("support_info").getString("url");
                pc_req=c.getJSONObject("pc_requirements").getString("recommended");
                mac_req=c.getJSONObject("mac_requirements").getString("recommended");
                linux_req=c.getJSONObject("linux_requirements").getString("recommended");
                descprtion=c.getString("detailed_description");


                //Toast.makeText(Game_Information_activity.this,"111"+screenshots_array[1] , Toast.LENGTH_SHORT).show();

                count=screenshots.length()+"";

            } catch (JSONException e) {
                e.printStackTrace();
            }
            kk=jsonStr;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            for(int i=0;i<10;i++)
            {
                switch (i){
                    case 0:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc1);
                    case 1:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc2);
                    case 2:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc3);
                    case 3:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc4);
                    case 4:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc5);
                    case 5:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc6);
                    case 6:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc7);
                    case 7:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc8);
                    case 8:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc9);
                    case 9:
                        Glide.with(getApplicationContext()).load(screenshots_array[i]).into(sc10);
                }

            }
            getSupportActionBar().setTitle(name+"");
            name_textview.setText(name);
            Glide.with(getApplicationContext()).load(header_image)
                    .into(iv);
            if(req_age!=null){
                if(Integer.parseInt(req_age)<=0)
                    age_textview.setText("No Age Limit");
                else
                    age_textview.setText(""+req_age);
            }
            else
                age_textview.setText("No Age Limit");
            String text = "Not Spec";
            if(controller_support!=null)
                text = String.valueOf(controller_support.charAt(0)).toUpperCase() + controller_support.subSequence(1, controller_support.length());
            con_support_textview.setText(""+text);
            tv_short_description.setText(short_description);
            if(support_languages!=null){
                support_languages=Html.fromHtml(support_languages)+"";
                support_languages = support_languages.replace("*", "");
                support_languages = support_languages.replace("\n", "");
            }

            if(linux_req!=null)
                linux_req=Html.fromHtml(linux_req)+"";
            if(pc_req!=null)
                pc_req=Html.fromHtml(pc_req)+"";
            if(mac_req!=null)
                mac_req=Html.fromHtml(mac_req)+"";
            tv_support_lan.setText(""+support_languages+".");
            tv_linux_req.setText(linux_req);
            tv_mac_req.setText(mac_req);
            tv_pc_req.setText(pc_req);
            if(linux_req==null&&mac_req==null&&pc_req==null)
            {
                req_layoout.setVisibility(View.GONE);
                req_text.setText("Requirements : Not Specified");
            }
            else {
                if (linux_req==null)
                    tv_linux_req.setText("Not Specified");
                if (pc_req==null)
                    tv_pc_req.setText("Not Specified");
                if (mac_req==null)
                    tv_mac_req.setText("Not Specified");
            }
            if (about_game!=null)
                about_game=Html.fromHtml(about_game)+"";
            tv_about_game.setText(about_game);
            if(descprtion!=null) {
                descprtion = Html.fromHtml(descprtion) + "";
                tv_descprtion.setText(descprtion);
            }
            else
                tv_descprtion.setText("No Descprtion");
            published.setText(release_date);
            support.setText(support_info);
           // Toast.makeText(Game_Information_activity.this,"222"+screenshots_array[1] , Toast.LENGTH_SHORT).show();
            pdLoading.dismiss();
        }

    }
}