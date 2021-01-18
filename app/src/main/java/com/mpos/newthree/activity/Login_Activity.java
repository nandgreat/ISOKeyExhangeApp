package com.mpos.newthree.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpos.newthree.helper.Vehicle_category;

import org.json.JSONObject;
import com.mpos.newthree.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by TECH-PC on 10/4/2018.
 */

public class Login_Activity extends AppCompatActivity {

    ConstantFile connect=new ConstantFile();

    com.xwray.passwordview.PasswordView passwordEdit;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView loginLabel;
    private ImageView logo;
    private TextView txt;
    EditText input_email;
    Context context;
    RequestQueue queue =null;
    public static List<Vehicle_category> categoryList=null;
    AppCompatButton btn_login;
    TextInputLayout phone1;
    TextInputLayout phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout2);

        context=getApplicationContext();
        input_email =(EditText)findViewById(R.id.input_email);
        passwordEdit=(com.xwray.passwordview.PasswordView)findViewById(R.id.pass);
        queue = Volley.newRequestQueue(this);
        btn_login = (AppCompatButton)findViewById(R.id.btn_login);
        phone1=(TextInputLayout)findViewById(R.id.phone1);
        phone2=(TextInputLayout)findViewById(R.id.phone2);
        txt = (TextView) findViewById(R.id.lodinText);
        loginLabel = (TextView) findViewById(R.id.loginLabel);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        ConstantFile.userPhone="";
        ConstantFile.wallet="";
        ConstantFile.FULL_NAME="";



        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_GO) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });



        Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
            draw = getResources().getDrawable(R.drawable.progress_dialog, getTheme());
        } else {
            draw = getResources().getDrawable(R.drawable.progress_dialog);
        }

        progressBar.setProgressDrawable(draw);
        progressBar.setVisibility(View.GONE);


    }
    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }
    }
    private void visible(){
        txt.setVisibility(View.VISIBLE);
        //passOne.setVisibility(View.GONE);
        //emailOne.setVisibility(View.GONE);
        input_email.setVisibility(View.GONE);
        passwordEdit.setVisibility(View.GONE);
        btn_login.setVisibility(View.GONE);
        loginLabel.setVisibility(View.GONE);
        btn_login.setEnabled(false);
        phone1.setVisibility(View.GONE);
        phone2.setVisibility(View.GONE);

    }
    private void gone(){
        txt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        //passOne.setVisibility(View.VISIBLE);
        //emailOne.setVisibility(View.VISIBLE);
        input_email.setVisibility(View.VISIBLE);
        passwordEdit.setVisibility(View.VISIBLE);
        loginLabel.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.VISIBLE);
        phone2.setVisibility(View.VISIBLE);
        phone1.setVisibility(View.VISIBLE);

        btn_login.setEnabled(true);
    }

    private void attemptLogin() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_login.getWindowToken(), 0);
        String email = input_email.getText().toString();
        String password = passwordEdit.getText().toString();
        // mEmailView.setText("07056691240");
        //mPasswordView.setText("123456");
        //email = "dothjiz@gmail.com";
        //password = "olandath201000";
        processData(email, password);
    }

    private void processData(String user, String pass) {
        if( (!user.isEmpty() && user != null)
                && (!pass.isEmpty() && pass != null) ){

            visible();
            progressBar.setVisibility(View.VISIBLE);

            Map<String, String> param = new HashMap<String, String>();
            param.put("phone", DESDESDES.encrypt(user));
            param.put("password", DESDESDES.encrypt(pass));

//            getUserLoginIfo(user,pass);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);



        }else{
            showMessage("Invalid username or password!");
        }
    }
    public void getUserLoginIfo(final String phone, String pass){

        Map<String, String> param= new HashMap<>();

        //System.out.println("phone "+phone+" pass "+pass);
        param.put("phone", phone);
        param.put("password", pass);

        //System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("login"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // System.out.println("response ll "+ Encryption._d(response));
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));

                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                                ParseJSonResponse(response);
                                System.out.println("after");
                                getUserWallet(phone);
                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));
                                gone();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }else{
                                gone();
                                showMessage(response.getString("response_message"));
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               gone();

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
    private void showMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void ParseJSonResponse(JSONObject object){
        JSONArray array=null;
        JSONArray arraycat=null;
        JSONObject obj=null;
        JSONObject objprofile=null;

        categoryList=new ArrayList<>();
        try {
            obj=object.getJSONObject("data");
            System.out.println("obj "+obj);
            ConstantFile.IS_OPERATOR=obj.getInt("is_operator");
            System.out.println(" ConstantFile.IS_OPERATOR "+ ConstantFile.IS_OPERATOR);
            ConstantFile.FULL_NAME=obj.getString("firstname")+" "+obj.getString("lastname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            objprofile=obj.getJSONObject("profile");
            System.out.println("profile "+objprofile);
            ConstantFile.userPhone=objprofile.getString("phone");
            System.out.println("phone "+objprofile.getString("phone"));
            System.out.println("phone "+ConstantFile.userPhone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arraycat=obj.getJSONArray("vehicle_categories");
            System.out.println("vehicle_categories "+arraycat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<arraycat.length(); i++) {

            Vehicle_category service = new Vehicle_category();

            JSONObject json = null;
            try {

                json = arraycat.getJSONObject(i);

                service.setCatid(json.getString("category_id"));
                service.setFee(json.getString("fee"));
                service.setName(json.getString("category_name"));
                System.out.println("name "+json.getString("category_name"));


            } catch (JSONException e) {

                e.printStackTrace();
            }
            categoryList.add(service);

        }






        try {



            ConstantFile.FULL_NAME=objprofile.getString("firstname")+" "+objprofile.getString("lastname");


        } catch (JSONException e) {

            e.printStackTrace();
        }




    }




    public void getUserWallet(final String phone){

        Map<String, String> param= new HashMap<>();


        param.put("phone", phone);

        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("getWalletBalance"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response wallet "+response);

                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                                ParseJSonWallet(response);
                                //ConstantFile.wallet=response.getString("wallet");
                            }else{
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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
    public void ParseJSonWallet(JSONObject object) {
        JSONArray array = null;
        JSONArray arraycat = null;
        JSONObject obj = null;
        JSONObject objprofile = null;


        try {
            obj = object.getJSONObject("data");
            System.out.println("wallet" + obj);
            ConstantFile.wallet=obj.getString("wallet_balance");
            System.out.println("bal "+obj.getString("wallet_balance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
