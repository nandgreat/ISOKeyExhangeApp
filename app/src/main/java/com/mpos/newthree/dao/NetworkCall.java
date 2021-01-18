package com.mpos.newthree.dao;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkCall {
    private Context ctx;
    private String url;
    private NetworkInterface networkInterface = null;
    private NetworkInterfaceImage networkInterfaceImage = null;
    private NetworkErrorInterface networkErrorInterface;
    private int method = -1;
    private String fileName;
    JSONObject param;
    private final int MY_SOCKET_TIMEOUT_MS = 2000;

    public NetworkCall(NetworkInterface networkInterface, NetworkErrorInterface networkErrorInterface,
                       Context ctx, String url){
        this.ctx = ctx;
        this.url = url;
        this.networkInterface = networkInterface;
        this.networkErrorInterface = networkErrorInterface;
    }

    public NetworkCall(NetworkInterface networkInterface, NetworkErrorInterface networkErrorInterface,
                       Context ctx, String url, JSONObject param){
        this.ctx = ctx;
        this.url = url;
        this.networkInterface = networkInterface;
        this.networkErrorInterface = networkErrorInterface;
        this.param = param;
        System.out.println("crash 2");
    }

    public NetworkCall(NetworkInterfaceImage networkInterfaceImage, NetworkErrorInterface networkErrorInterface, String url, String fileName){
        this.url = url;
        this.fileName = fileName;
        this.networkInterfaceImage = networkInterfaceImage;
        this.networkErrorInterface = networkErrorInterface;
    }



    public void makeCall(String verb){

        switch (verb){
            case  "GET":
                method = Request.Method.GET;
                noData();
                break;
            case  "POST":
                method = Request.Method.POST;
                data();
                break;
            case  "PUT":
                method = Request.Method.PUT;
                data();
                break;
            case  "DELETE":
                method = Request.Method.DELETE;
                noData();
                break;
        }


    }

    public void fetchImage(){
        ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                networkInterfaceImage.processNetwokPostExecute(response, fileName);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    networkErrorInterface.processNetworkErrorInterface(error);
                }
            });
    }

    private void data(){
        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, param,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        networkInterface.processNetworkPostExecute(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                networkErrorInterface.processNetworkErrorInterface(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(jsonRequest);
    }

    private  void noData(){

       /* JsonObjectRequest jsonRequest = new JsonObjectRequest
            (method, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("onResponse", response.toString());
                    networkInterface.processNetworkPostExecute(response);
                }
                
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkErrorInterface.processNetworkErrorInterface(error);
                }
            }){
                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(jsonRequest);*/
    }

    public interface NetworkInterface {
        void processNetworkPostExecute(JSONObject response);
    }

    public interface NetworkInterfaceImage {
        void processNetwokPostExecute(Bitmap response, String fileName);
    }

    public interface NetworkErrorInterface {
        void processNetworkErrorInterface(VolleyError error);
    }

}
