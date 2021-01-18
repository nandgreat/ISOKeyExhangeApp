package com.mpos.newthree.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpos.newthree.R;
import com.mpos.newthree.adapter.Log_Adapter;
import com.mpos.newthree.helper.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class TransactionLog  extends AppCompatActivity{
    ConstantFile connect=new ConstantFile();
    Context context;
    RecyclerView mRecyclerView;
    private View mProgressView;
    RequestQueue queue =null;
    public List<History> ticketList;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        context = getApplicationContext();
        getSupportActionBar().setTitle("TRANS HISTORY");
        mRecyclerView = (RecyclerView) findViewById(R.id.listLog);
        progressBarHolder = (FrameLayout) findViewById(R.id.progress_overlay);
        mProgressView = findViewById(R.id.progress);
        queue = Volley.newRequestQueue(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getTransactionHistory(ConstantFile.userPhone);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRecyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void getTransactionHistory(String phone){
        inAnimation = new AlphaAnimation(0f, 1f);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        Map<String, String> param= new HashMap<>();


        param.put("phone", phone);


        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("getTransactionHistory"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));
                            showProgress(false);
                            if(response.getString("response_code").equalsIgnoreCase("200")) {

                                System.out.println("after");
                                ParseJSonResponse(response);
                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));


                            }else{
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_LONG).show();

                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                outAnimation = new AlphaAnimation(1f, 0f);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed: Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                return headers;
            }



        };



        queue.add(jsonObjReq);


    }
    public void ParseJSonResponse(JSONObject object){

        JSONArray ticket=null;
        JSONObject obj=null;


        ticketList=new ArrayList<>();
        try {
            obj=object.getJSONObject("data");
            System.out.println("obj "+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ticket=obj.getJSONArray("history");
            System.out.println("ticket "+ticket);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<ticket.length(); i++) {

            History service = new History();

            JSONObject json = null;
            try {

                json = ticket.getJSONObject(i);

                service.setAmountpaid(json.getString("amount_payable"));
                service.setCategory(json.getString("vehicle_category"));
                service.setMember_id(json.getString("member_id"));
                service.setPaid_on(json.getString("paid_on"));
                service.setPayment_feedback(json.getString("payment_feedback"));
                service.setStatus(json.getString("status"));
                service.setPayment_mode(json.getString("payment_mode"));
                service.setTicketid(json.getString("ticket_id"));

                service.setChasis(json.getString("vehicle_chaisis"));
                service.setMake(json.getString("vehicle_make"));
                service.setPlateno(json.getString("vehicle_plate"));
                service.setDriver(json.getString("driver_name"));




            } catch (JSONException e) {

                e.printStackTrace();
            }
            ticketList.add(service);

        }

        mRecyclerView.setAdapter(new Log_Adapter(ticketList, context));
        outAnimation = new AlphaAnimation(1f, 0f);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);

    }
}
