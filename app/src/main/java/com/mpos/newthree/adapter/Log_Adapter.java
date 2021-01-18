package com.mpos.newthree.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mpos.newthree.R;
import com.mpos.newthree.activity.ActivityTransactionCompleted;
import com.mpos.newthree.activity.ConstantFile;
import com.mpos.newthree.helper.History;
import com.mpos.newthree.wizarpos.emvsample.printer.PrinterException;
import com.mpos.newthree.wizarpos.jni.PrintSlip;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mpos.newthree.activity.CardPayActivity.tId;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class Log_Adapter extends RecyclerView.Adapter<Log_Adapter.ViewHolder> {
    List<History> subjects;

    Context context;
    ViewHolder holder1=null;
    public int mPosition=0;
    History getDataAdapter1 =null;
    // private final List<GetJSONResponse.Content.SubMenu.Item> mValues;


    public Log_Adapter( List <History> getDataAdapter, Context context) {
        subjects = getDataAdapter;
        this. context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder1=holder;
        mPosition=position;
        getDataAdapter1 =  subjects.get(position);
        if(getDataAdapter1!=null) {

            holder.description.setText(getDataAdapter1.getDriver());
            if (!getDataAdapter1.getPayment_feedback().equalsIgnoreCase("") && getDataAdapter1.getPayment_feedback() != null) {
               // System.out.println("des "+getDataAdapter1.getDescription());
                holder.amount.setText(getDataAdapter1.getPlateno());
            }
            holder.date.setText(getDataAdapter1.getPaid_on());
            holder.pin.setText(getDataAdapter1.getTicketid());
            holder.transtate.setText(getDataAdapter1.getStatus());
            holder.driver.setText(getDataAdapter1.getMember_id());
            holder.mamount.setText(getDataAdapter1.getAmountpaid());
            holder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView b = (TextView)view.findViewById(R.id.tv_pin);
                  //  System.out.println("id= "+holder1.pin.getText().toString());
                    ActivityTransactionCompleted.paymentMedium=getDataAdapter1.getPayment_mode();
                    PrintSlip.transactionStatus=getDataAdapter1.getStatus();
                    PrintSlip.date=getDataAdapter1.getPaid_on();
                    ConstantFile.category=getDataAdapter1.getCategory();
                    ConstantFile.amount=getDataAdapter1.getAmountpaid();
                    PrintSlip.name=getDataAdapter1.getDriver();
                    PrintSlip.plateNo=getDataAdapter1.getPlateno();
                    PrintSlip.ticketid=getDataAdapter1.getTicketid();
                    ConstantFile.chasis=getDataAdapter1.getChasis();
                    try {
                        printReceipt();
                    } catch (PrinterException e) {
                        e.printStackTrace();
                    }
                 /*   ActivityTransactionCompleted. reprintid= holder1.pin.getText().toString();
                    ActivityTransactionCompleted.reprint=true;
                    Intent intent = new Intent (view.getContext(), ActivityTransactionCompleted.class);
                    intent.putExtra("status","Reprint Transaction( "+holder1.pin.getText().toString()+" )");
                    context.startActivity(intent);*/


                }
            });
        }



    }

    @Override
    public int getItemCount() {

        return subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public TextView amount;
        public TextView date;
        public TextView pin;
        public TextView transtate;
        public TextView driver;
        public TextView mamount;

        public Button print;


        public ViewHolder(View itemView) {

            super(itemView);
            mamount = (TextView) itemView.findViewById(R.id.amount) ;
            description = (TextView) itemView.findViewById(R.id.tv_desc) ;
            amount = (TextView) itemView.findViewById(R.id.tv_amt) ;
            date = (TextView) itemView.findViewById(R.id.tv_date) ;
            pin = (TextView) itemView.findViewById(R.id.tv_pin) ;
            transtate=(TextView)itemView.findViewById(R.id.transtate);
            driver=(TextView)itemView.findViewById(R.id.driver);
            print=(Button)itemView.findViewById(R.id.print);

        }
    }
    synchronized public void printReceipt() throws PrinterException
    {

        try {
            final PrintSlip print = new PrintSlip();
            int open = print.open();
            if(open >= 0){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.oyoimage);
                print.writesBitmap(bitmap);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                final String str = formatter.format(curDate);
                PrintSlip.tId= tId;
                //print.writeLineBreak(10);



                        print.writesBill();//BAL_INQ


                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.onepay);
                print.writesBitmap(bitmap);
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.printend);
                print.writesBitmap(bitmap);


            }else{
               // Toast.makeText(ActivityTransactionCompleted.this,"Printer failed to open",Toast.LENGTH_LONG).show();
            }


        } catch (UnsupportedEncodingException e) {
            throw new PrinterException("PrinterHelper.printReceipt():" + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new PrinterException(e.getMessage(), e);
        }
    }

}
