package com.example.desilyft;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sriablaze on 18/01/19.
 */

public class Http {

    private static String HOST = "http://10.10.11.153:8000";
    private static String AUTH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTUxNzE1NTc0LCJqdGkiOiJjOTRlMWQ2MTk3MTg0N2I0OTkwNjYwMzhiMWMwMTE2MyIsInVzZXJfaWQiOjF9.eho4hKKv9us0unBAXeE216BtQQPxDzWip3-htRAUGvM";

    public static void hit(String url, JSONObject data, final Callback callbackClass) {
        String completeURL = HOST + url;
        JsonObjectRequest req = new JsonObjectRequest(completeURL, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("json response", response.toString());
                            HashMap<String, Object> responseHashMap = new ObjectMapper().readValue(response.toString(), HashMap.class);
                            Log.d("hashmap response", responseHashMap.toString());
                            callbackClass.handleResponse(responseHashMap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + AUTH_TOKEN);
                params.put("Content-Type", "application/json");
                Log.d("params", params.toString());
                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(req);
    }
}
