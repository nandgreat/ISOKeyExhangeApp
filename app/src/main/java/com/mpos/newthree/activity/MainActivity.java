package com.mpos.newthree.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mpos.newthree.R;
import com.mpos.newthree.adapter.CustomItemClickListener;
import com.mpos.newthree.adapter.HomeItemAdapter;
import com.mpos.newthree.dao.TestIso;
import com.mpos.newthree.helper.Home_Item;
//import com.mpos.newtwo.wizarpos.emvsample.activity.FuncActivity;
import com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity;
import com.mpos.newthree.wizarpos.jni.ContactICCardReaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mpos.newthree.dao.ISOMessage.terminalId;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.appState;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.loadCAPK;
import static com.mpos.newthree.wizarpos.emvsample.constant.Constant.APP_TAG;

/**
 * Created by TECH-PC on 10/4/2018.
 */

public class MainActivity extends AppCompatActivity {
    ConstantFile connect=new ConstantFile();
    public static final String MyPREFERENCES = "MyPrefs" ;
    FuncActivity func;
    public static Context context;
    public static boolean Q2=false;
    public static boolean timeout;
    public  static String AESKey;
    public static String card_check = "502192-517731";
    RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    RequestQueue queue =null;
    static  String[] MOBILE_OS ={};

    public static String selectOperation="";
    String driverId="";
    private IntentIntegrator qrScan;
    private String QR_CODE;
    public List<Home_Item> homeList;
    HomeItemAdapter adapter;
    TextView accBal;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        context = this;

        func = new FuncActivity(this);
//makePaymentViaWallet
        accBal=(TextView)findViewById(R.id.accBal);
        queue = Volley.newRequestQueue(context);
        progressBarHolder = (FrameLayout) findViewById(R.id.progress_overlay);

        //getDriver("");
       // makePaymentViaWallet( "10001","07056691240","123456");
        getUserWallet(ConstantFile.userPhone);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycleritem);
        qrScan = new IntentIntegrator(this);
        try {

            if (appState.icInitFlag == false) {

                if (ContactICCardReaderInterface.init() >= 0) {
                    Log.d(APP_TAG, "ContactICCardReaderInterface.init() OK");
                    appState.icInitFlag = true;

                    TestIso testIso = new TestIso(getApplication(),terminalId);
                    testIso.themains();
                    Q2 = true;
                }
            }

        }catch (UnsatisfiedLinkError e){
            Q2 = false;
            e.printStackTrace();
        }

        System.out.println("Q2 "+Q2);
        if (ConstantFile.IS_OPERATOR==1) {
            homeList=new ArrayList<>();
            Home_Item item1=new Home_Item("01","VERIFY DRIVER");
            homeList.add(item1);
            Home_Item item2=new Home_Item("02","TRANSACTION LOG");
            homeList.add(item2);
            Home_Item item3=new Home_Item("03","CHANGE PASSWORD");
            homeList.add(item3);
            Home_Item item4=new Home_Item("07","LOG OUT");
            homeList.add(item4);

        } else if (ConstantFile.IS_OPERATOR==0) {
            homeList=new ArrayList<>();
            Home_Item item1=new Home_Item("04","Loan Application");
            homeList.add(item1);
            Home_Item item2=new Home_Item("05","Guarantor Request");
            homeList.add(item2);
            Home_Item item3=new Home_Item("06","Monthly Report");
            homeList.add(item3);
            Home_Item item4=new Home_Item("08","View Payment");
            homeList.add(item4);
            Home_Item item5=new Home_Item("08","View Profile");
            homeList.add(item5);
            Home_Item item6=new Home_Item("08","Log Out");
            homeList.add(item6);

        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.setAdapter(new HomeItemAdapter(homeList, context));
        adapter = new HomeItemAdapter(homeList, context, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                String id  = homeList.get(position).getName();
                System.out.println("id "+id);
                if(homeList.get(position).getId().equalsIgnoreCase("01")){
                    getMethod();
                }else if(homeList.get(position).getId().equalsIgnoreCase("02")){

                    Intent intent=new Intent(MainActivity.this,TransactionLog.class);
                    startActivity(intent);
                }else if(homeList.get(position).getId().equalsIgnoreCase("03")){
                    Intent intent=new Intent(MainActivity.this,ChangePassword.class);
                    startActivity(intent);

                }else if(homeList.get(position).getId().equalsIgnoreCase("04")){
                    Intent intent=new Intent(MainActivity.this,ChangeVehicleCategory.class);
                    startActivity(intent);

                }else if(homeList.get(position).getId().equalsIgnoreCase("05")){
                    Intent intent=new Intent(MainActivity.this,TransactionLog.class);
                    startActivity(intent);

                }else if(homeList.get(position).getId().equalsIgnoreCase("06")){
                    Intent intent=new Intent(MainActivity.this,ChangePassword.class);
                    startActivity(intent);

                }else if(homeList.get(position).getId().equalsIgnoreCase("07")){
                    Intent intent=new Intent(MainActivity.this,Login_Activity.class);
                    startActivity(intent);

                }
                else if(homeList.get(position).getId().equalsIgnoreCase("08")){
                    Intent intent=new Intent(MainActivity.this,Login_Activity.class);
                    startActivity(intent);

                }
                // do what ever you want to do with it
            }
        });
        recyclerView.setAdapter(adapter);
if(!ConstantFile.wallet.equalsIgnoreCase("")) {
    accBal.setText("Balance: " + ConstantFile.wallet);
}
    }
    @Override
    public void onStart() {

        Log.e(APP_TAG, "Login onStart");
        super.onStart();
        appState.initData();
        appState.idleFlag = true;
        if (appState.emvParamLoadFlag == false) {
            try {
                loadEMVParam();
            } catch (UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        } else {
            if (appState.emvParamChanged == true) {
                func.setEMVTermInfo();
            }
        }

    }

    public void loadEMVParam()
    {
//
        if(func.loadEMVKernel() == 0)
        {
            func.emv_kernel_initialize();
            func.emv_set_kernel_attr(new byte[]{0x20}, 1);

            if(loadCAPK() == -2)
            {
                func.capkChecksumErrorDialog(this);
            }
            func.loadAID();
            func.loadExceptionFile();
            func.loadRevokedCAPK();
            func.setEMVTermInfo();

            func.emv_set_force_online(appState.terminalConfig.getforceOnline());
            appState.emvParamLoadFlag = true;
        }
//
    }
    public void getMethod(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.category_dialog);
       // dialog.setTitle("Select Verification Medium");

        // set the custom dialog components - text, image and button
       // TextView text = (TextView) dialog.findViewById(R.id.text);
       // text.setText("Android custom dialog example!");
        CardView image = (CardView) dialog.findViewById(R.id.history);
        CardView scan = (CardView) dialog.findViewById(R.id.scan);


       // Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                System.out.println("enter");
                inputDriverId();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                System.out.println("scan");
                qrScan.initiateScan();
            }
        });

        dialog.show();

    }

    public void getDriver( String driver){
        inAnimation = new AlphaAnimation(0f, 1f);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        Map<String, String> param= new HashMap<>();

      // driver="10001";
        param.put("driver_id", driver);


        System.out.println(" getDriverDetails params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("getDriverDetails"), new JSONObject(param),
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
                                ParseJSonResponse(response);
                                outAnimation = new AlphaAnimation(1f, 0f);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));

                                Intent intent = new Intent(getApplicationContext(), Verify_Driver.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


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
    public void makePaymentViaWallet( String driver,String phone,String pass){

        Map<String, String> param= new HashMap<>();

        driver="10001";
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

                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }else{
                                //  showMessage(response.getString("response_message"));
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // gone();
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

    protected void inputDriverId(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Driver Id");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                driverId = input.getText().toString();
                try {
                    System.out.println("driver= "+driverId);
                    getDriver(driverId);
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void ParseJSonResponse(JSONObject object) {
        JSONArray array = null;
        JSONArray arraycat = null;
        JSONObject obj = null;
        JSONObject objprofile = null;


        try {
            obj = object.getJSONObject("data");
            System.out.println("obj driver "+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("catv "+obj.getString("vehicle_category"));
            ConstantFile.category=obj.getString("vehicle_category");
            ConstantFile.driverpasspost=obj.getString("passport");
            ConstantFile.driver_id=obj.getString("driver_id");
            ConstantFile.driver_fname=obj.getString("firstname");
            ConstantFile.amount=obj.getString("amount");
            ConstantFile.chasis=obj.getString("vehicle_chaisis");
            ConstantFile.driver_lastname=obj.getString("lastname");
            ConstantFile.email=obj.getString("email");
            ConstantFile.plate=obj.getString("vehicle_plate");
            ConstantFile.make=obj.getString("vehicle_make");
            Intent intent = new Intent(getApplicationContext(), Verify_Driver.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }catch(Exception ed){}

    }
    public static String formatCurrency(String amount){
        System.out.println("amount main activity "+amount);
        try {
            // The comma in the format specifier does the trick
            amount = "N"+ String.format("%,.2f", Float.parseFloat(amount));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return amount;
        }

        return amount;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());




                } catch (JSONException e) {
                    e.printStackTrace();
                    QR_CODE=result.getContents();
                    System.out.println("QR_CODE "+QR_CODE);
                    getDriver(QR_CODE);
                    // buttonScan.setVisibility(View.INVISIBLE);
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    // Toast.makeText(this, "r"+result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
