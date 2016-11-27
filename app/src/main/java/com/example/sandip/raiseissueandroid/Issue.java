package com.example.sandip.raiseissueandroid;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sandip on 10/31/2016.
 */

public class Issue extends AppCompatActivity {

    ArrayAdapter<String> adapter_issue;
    String issue_numbers[] = {"Garbage", "Potholes", "Speed Breakers", "Street Light Not Working", "Traffic", "Water Logging", "Other", "None"};
    String id;
    Spinner element;
    Button submit;
    String url = "Your URL";
    public static final String KEY_ID = "issueid";
    public static final String KEY_ISSUE = "issue";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);
        Intent pre = getIntent();
        id = pre.getStringExtra("id");
        element = (Spinner) findViewById(R.id.spinner);
        adapter_issue = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, issue_numbers);
        adapter_issue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView txt = (TextView) findViewById(R.id.txtView);
        txt.setText("Issue");
        element.setAdapter(adapter_issue);
        element.setSelection(Integer.parseInt(pre.getStringExtra("issue"))-1);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        final int pos = element.getSelectedItemPosition();
        final int position=pos+1;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent next=new Intent(Issue.this,MainActivity.class);
                        next.putExtra("id",id);
                        startActivity(next);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Issue.this, "Something Went Wrong", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, id);
                params.put(KEY_ISSUE, String.valueOf(position));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
