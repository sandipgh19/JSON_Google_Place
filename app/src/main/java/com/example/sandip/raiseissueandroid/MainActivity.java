package com.example.sandip.raiseissueandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class MainActivity extends AppCompatActivity {
    ImageButton status_btn, description_btn, issue_btn, address_btn;
    TextView id_view, name_view, status_view, description_view, address_view, date_time_view, uploader_view, issue_view;
    ImageView img;
    String name,status,issue,description,address,lat,lng;
    String id= String.valueOf(1);
    Integer position=Integer.valueOf(id)-1;
    String issue_numbers[] = {"Garbage", "Potholes", "Speed Breakers", "Street Light Not Working", "Traffic", "Water Logging", "Other", "None"};
    String status_numbers[] = {"Pending", "Resolved"};
    private static String url_data = "http://raiseissue.com/img/api.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        Intent pre=getIntent();
        id=pre.getStringExtra("id");
        if(id!=null){
            position= Integer.valueOf(id)-1;
        }
        img = (ImageView) findViewById(R.id.imageView);
        status_btn = (ImageButton) findViewById(R.id.status_img);
        description_btn = (ImageButton) findViewById(R.id.description_img);
        issue_btn = (ImageButton) findViewById(R.id.issue_img);
        address_btn = (ImageButton) findViewById(R.id.address_img);
        id_view = (TextView) findViewById(R.id.id);
        name_view = (TextView) findViewById(R.id.name);
        status_view = (TextView) findViewById(R.id.status);
        address_view = (TextView) findViewById(R.id.address);
        issue_view = (TextView) findViewById(R.id.issue);
        description_view = (TextView) findViewById(R.id.description);
        date_time_view = (TextView) findViewById(R.id.date_time);
        uploader_view = (TextView) findViewById(R.id.uploaer);
        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, Status_new.class);
                next.putExtra("id",id);
                next.putExtra("status",status);
                startActivity(next);
            }
        });
        issue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next1 = new Intent(MainActivity.this, Issue.class);
                next1.putExtra("id",id);
                next1.putExtra("issue",issue);
                startActivity(next1);
            }
        });
        address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next2 = new Intent(MainActivity.this, Location.class);
                next2.putExtra("id",id);
                next2.putExtra("address",address);
                next2.putExtra("lat",lat);
                next2.putExtra("lng",lng);
                startActivity(next2);
            }
        });
        description_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next3 = new Intent(MainActivity.this, description.class);
                next3.putExtra("id",id);
                next3.putExtra("description",description);
                startActivity(next3);
            }
        });
        getData();
        getImage();

    }
    private void getData(){
        StrictMode.ThreadPolicy policy=new  StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();

        StrictMode.setThreadPolicy(policy);

        org.json.JSONObject json = null;
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpPost myConnection = new HttpPost(url_data);

        try {
            response = myClient.execute(myConnection);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jArray = new JSONArray(str);
            json = jArray.getJSONObject(position);

            id=json.getString("id");
            id_view.setText(id);
            name=json.getString("name");
            name_view.setText(name);
            issue=json.getString("issue");
            issue_view.setText(issue_numbers[(Integer.parseInt(issue)-1)]);
            address=json.getString("address");
            address_view.setText(address);
            lat=json.getString("lat");
            lng=json.getString("lng");
            description=json.getString("description");
            description_view.setText(description);
            status=json.getString("status");
            status_view.setText(status_numbers[(Integer.parseInt(status))]);
            uploader_view.setText(json.getString("uploadedby"));
            date_time_view.setText(json.getString("datetime"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getImage() {

        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                img.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String add = "http://raiseissue.com/images/uploads/"+name;
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(name);
    }
    public void onBackPressed(){
        finish();
        System.exit(0);
    }

}