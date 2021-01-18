package com.mpos.newthree.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
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
import com.mpos.newthree.adapter.SpinnerAdapter;
import com.mpos.newthree.helper.Vehicle_category;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class ChangeVehicleCategory extends AppCompatActivity {
    ConstantFile connect=new ConstantFile();
    Context context;
    EditText plateno;
   MaterialBetterSpinner category;
EditText make;
EditText chasis;
EditText password;
Button email_sign_in_button;
    SpinnerAdapter adapter;
    String selectedCategory="";
    RequestQueue queue =null;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_vehicle_category);
        plateno=(EditText) findViewById(R.id.plateno);
        getSupportActionBar().setTitle("Change category");
        category=(MaterialBetterSpinner)findViewById(R.id.category) ;
        progressBarHolder = (FrameLayout) findViewById(R.id.progress_overlay);
        queue = Volley.newRequestQueue(this);
        make=(EditText)  findViewById(R.id.make);
        chasis=(EditText) findViewById(R.id.chasis);
        password=(EditText)  findViewById(R.id.password);
        email_sign_in_button=(Button)findViewById(R.id.email_sign_in_button);
        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plateno.getText().toString().equalsIgnoreCase("")&& !make.getText().toString().equalsIgnoreCase("") && selectedCategory!="" && selectedCategory!=null){

                    getUserLoginIfo(plateno.getText().toString(), chasis.getText().toString(),make.getText().toString(),selectedCategory,ConstantFile.userPhone,password.getText().toString());


                }

            }
        });


        context = getApplicationContext();
        try {
            if(Login_Activity.categoryList.size()>0) {
                System.out.println("size "+Login_Activity.categoryList.size());
                adapter = new SpinnerAdapter(this,
                        R.layout.spinner_layout, R.id.txt, Login_Activity.categoryList);
                category.setAdapter(adapter);
            }
        }catch(Exception ex){}


        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("position qes "+i);
                Vehicle_category user = Login_Activity.categoryList.get(i);
                //System.out.println("question=+ "+user.getText());
                selectedCategory=user.getCatid();


            }
        });
    }



    public void getUserLoginIfo(String plate_no, String chaisis_no,String make_number,String vehicle_category_id,String phone,String password){
        inAnimation = new AlphaAnimation(0f, 1f);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        Map<String, String> param= new HashMap<>();


        param.put("plate_no", plate_no);
        param.put("chaisis_no", chaisis_no);
        param.put("make_no", make_number);
        param.put("vehicle_category_id", vehicle_category_id);
        param.put("phone", phone);
        param.put("password", password);


        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("changeVehicleCategory"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
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
