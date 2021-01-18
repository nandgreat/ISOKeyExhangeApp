package com.mpos.newthree.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mpos.newthree.R;
import com.mpos.newthree.wizarpos.jni.PrintSlip;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mpos.newthree.activity.MainActivity.Q2;
import static com.mpos.newthree.wizarpos.jni.PrintSlip.transactionid;

public class Verify_Driver extends AppCompatActivity {
    Context context;
    ConstantFile connect=new ConstantFile();
    RequestQueue queue =null;
    TextView driverid,fname,lname,category,chasis,plate,make,amount;
    Button btn_wallet,btn_card;
    String password;
    AlertDialog mScanningDialog = null;
    ImageView usericon;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_detail);
        context = getApplicationContext();
        getSupportActionBar().setTitle("Driver Profile");
        queue = Volley.newRequestQueue(this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progress_overlay);
        btn_wallet=(Button)findViewById(R.id.btn_wallet);
        btn_card=(Button)findViewById(R.id.btn_card);
        driverid=(TextView)findViewById(R.id.driverid);
        fname=(TextView)findViewById(R.id.fname);
        lname=(TextView)findViewById(R.id.lname);
        chasis=(TextView)findViewById(R.id.chasis);
        category=(TextView)findViewById(R.id.category);
        plate=(TextView)findViewById(R.id.plate);
        make=(TextView)findViewById(R.id.make);
        amount=(TextView)findViewById(R.id.amount);
        driverid.setText(ConstantFile.driver_id);
        fname.setText(ConstantFile.driver_fname);
        lname.setText(ConstantFile.driver_lastname);
        category.setText(ConstantFile.category);
        chasis.setText(ConstantFile.chasis);
        plate.setText(ConstantFile.plate);
        make.setText(ConstantFile.make);
        amount.setText(ConstantFile.amount);
        usericon=(ImageView)findViewById(R.id.usericon);
        if(Q2==false){
            btn_card.setVisibility(View.GONE);
        }else{
            btn_card.setVisibility(View.VISIBLE);
        }
        btn_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTransactionCompleted.paymentMedium="wallet";
                CardPayActivity.transType = "BILL_PAY";
                showWalletDialog(view);
            }
        });
        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTransactionCompleted.paymentMedium="Card";
                createAccountTypeWindow();
            }
        });
        try {
            System.out.println("tee "+ConstantFile.driverpasspost);
            Glide.with(context).load(ConstantFile.driverpasspost).into(usericon);
        }catch(Exception ex){}

    }
    public void beginPaymentViaCard( String driverid, String phone,String stan,String pan){

        Map<String, String> param= new HashMap<>();


        param.put("driver_id", driverid);
        param.put("phone", phone);
        param.put("stan", stan);
        param.put("pan", pan);

        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("login"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));

                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                              //  ParseJSonResponse(response);
                                System.out.println("after");

                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }else{
                               // showMessage(response.getString("response_message"));
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


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

    public void makePaymentViaWallet( String driver,String phone,String pass){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String str = formatter.format(curDate);
        Map<String, String> param= new HashMap<>();
        inAnimation = new AlphaAnimation(0f, 1f);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        //driver="10001";
        param.put("driver_id", driver);
        param.put("phone", phone);
        param.put("password", pass);




        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("makePaymentViaWallet"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));
                            // gone();
                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                                System.out.println("after");
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));
                                try {
                                    JSONObject    obj=response.getJSONObject("data");
                                    JSONObject objticket=obj.getJSONObject("ticket");
                                    System.out.println("objticket "+objticket);
                                    System.out.println("transid: "+transactionid);
                                   // LoginActivity.userMainAmount = String.valueOf(Double.valueOf(LoginActivity.userMainAmount) - deduction);
                                    PrintSlip.amount = ConstantFile.amount;
                                    PrintSlip.name = ConstantFile.driver_fname;
                                    PrintSlip.phone = ConstantFile.plate;
                                    PrintSlip.transactionStatus = "Transaction Completed Successfully";
                                    ActivityTransactionCompleted.responseCode = "00";
                                    PrintSlip.ticketid =objticket.getString("ticket_id");
                                    PrintSlip.date = str;
                                }catch(Exception vb){}
                                Intent ite = new Intent(Verify_Driver.this, ActivityTransactionCompleted.class);
                                ite.putExtra("status","Transaction Completed Successfully");
                                startActivity(ite);

                            }else{
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_LONG).show();

                                //  showMessage(response.getString("response_message"));
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // gone();
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

    private void showWalletDialog(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Vuvaa wallet payment");
        alert.setMessage("Payment will be made through your Vuvaa wallet Are you sure you want to continue?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inputePassword();
            }
        });

        alert.show();
    }



    protected void inputePassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password = input.getText().toString();
                if(!password.isEmpty()&& !password.equalsIgnoreCase("")){
                    try {
                        makePaymentViaWallet( ConstantFile.driver_id,ConstantFile.userPhone,password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}else{

                    Toast.makeText(getApplicationContext(),"Please Enter your password",Toast.LENGTH_LONG).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void createAccountTypeWindow() {
        // if (!etName.getText().toString().equalsIgnoreCase("") && etName.getText() != null && !etPhone.getText().toString().equalsIgnoreCase("") && etPhone.getText() != null) {
        //System.out.println("name: "+etName.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(Verify_Driver.this);
        builder.setTitle("Account Type");
        View dv = getLayoutInflater().inflate(R.layout.account_dialog, null);
        final Spinner acc = dv.findViewById(R.id.acount_value);
        acc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (acc.getSelectedItem().toString().equals("SAVINGS")) {
                    CardPayActivity.accountType = "10";
                } else if (acc.getSelectedItem().toString().equals("CURRENT")) {
                    CardPayActivity.accountType = "20";
                } else if (acc.getSelectedItem().toString().equals("CREDIT")) {
                    CardPayActivity.accountType = "30";
                }
                if (!acc.getSelectedItem().toString().equals("SELECT")) {

                    lunchCardPayment("");


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(dv);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // dialog.dismiss();
                destroyScanningWindow();
            }
        });

        mScanningDialog = builder.create();
        mScanningDialog.show();


    }
    public void destroyScanningWindow()
    {
        if (mScanningDialog != null)
        {
            mScanningDialog.dismiss();
        }
    }
    private void lunchCardPayment(String transType) {
        CardPayActivity.transType = "BILL_PAY";
        CardPayActivity.amt = ConstantFile.amount;

        CardPayActivity.phone = "09098876567";

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String str = formatter.format(curDate);

       // PrintSlip.tId= tid;
        PrintSlip.amount = ConstantFile.amount;
        PrintSlip.plateNo = ConstantFile.plate;
        PrintSlip.name = ConstantFile.driver_fname;
        PrintSlip.phone = "09098876567";
        PrintSlip.date =str;
        Intent intent = new Intent(Verify_Driver.this,CardPayActivity.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        Intent setIntent = new Intent(Verify_Driver.this,MainActivity.class);

        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);

        return;
    }

}
