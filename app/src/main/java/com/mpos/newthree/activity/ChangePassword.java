package com.mpos.newthree.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class ChangePassword extends AppCompatActivity {
    ConstantFile connect=new ConstantFile();
    Context context;
    RequestQueue queue =null;
    EditText oldpassword;
    EditText newpassword;
    EditText comfirm;
    Button email_sign_in_button;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        context = getApplicationContext();
        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle("Change Password");
        progressBarHolder = (FrameLayout) findViewById(R.id.progress_overlay);
        oldpassword=(EditText)findViewById(R.id.oldpassword);
        newpassword=(EditText)findViewById(R.id.newpassword);
        comfirm=(EditText)findViewById(R.id.comfirm);
        email_sign_in_button=(Button)findViewById(R.id.email_sign_in_button);
        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!oldpassword.getText().toString().equalsIgnoreCase("")&& !newpassword.getText().toString().equalsIgnoreCase("")){
                 if(newpassword.getText().toString().equalsIgnoreCase(comfirm.getText().toString())) {
                     getUserLoginIfo(oldpassword.getText().toString(), newpassword.getText().toString(), comfirm.getText().toString(), ConstantFile.userPhone);
                 }else{
                    // System.out.println("--- "+newpassword.getText().toString());
                     Toast.makeText(getApplicationContext(), "new Password and Comfirm Password does not match", Toast.LENGTH_LONG).show();

                 }
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void getUserLoginIfo(String old, String newpass,String comfirm,String phone){
        inAnimation = new AlphaAnimation(0f, 1f);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        Map<String, String> param= new HashMap<>();


        param.put("phone", phone);
        param.put("old_password", old);
        param.put("new_password", newpass);
        param.put("comfirm_password", comfirm);



        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("changePassword"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("code  return"+response.getString("response_code"));

                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                System.out.println("after");
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_LONG).show();

                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));



                            }else{
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_LONG).show();

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
}
