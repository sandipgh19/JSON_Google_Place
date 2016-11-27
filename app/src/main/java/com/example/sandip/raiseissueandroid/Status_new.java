package com.example.sandip.raiseissueandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by sandip on 11/2/2016.
 */

public class Status_new extends AppCompatActivity {
    ArrayAdapter<String> adapter_status;
    String status_numbers[] = {"Pending", "Resolved"};
    String id;
    Spinner element;
    Button submit;
    String url = "Your URL";
    public static final String KEY_ID = "statusid";
    public static final String KEY_STATUS= "status";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);
        Intent pre=getIntent();
        id=pre.getStringExtra("id");
        element = (Spinner) findViewById(R.id.spinner);
        adapter_status = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status_numbers);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView txt = (TextView) findViewById(R.id.txtView);
        txt.setText("Status");
        element.setAdapter(adapter_status);
        element.setSelection(Integer.parseInt(pre.getStringExtra("status")));
        submit= (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

            }
        });
    }
    private void getData() {
        final int pos_status = element.getSelectedItemPosition();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent next=new Intent(Status_new.this,MainActivity.class);
                        next.putExtra("id",id);
                        startActivity(next);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Status_new.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, id);
                params.put(KEY_STATUS, String.valueOf(pos_status));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
