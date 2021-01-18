package com.mpos.newthree.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.newthree.R;
import com.mpos.newthree.helper.Home_Item;

import java.util.List;

/**
 * Created by TECH-PC on 10/4/2018.
 */

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {
    List<Home_Item> subjects;

    Context context;
    ViewHolder holder1=null;
    public int mPosition=0;
    Home_Item getDataAdapter1 =null;
    // private final List<GetJSONResponse.Content.SubMenu.Item> mValues;

    CustomItemClickListener listener;

    public HomeItemAdapter( List <Home_Item> getDataAdapter, Context context,CustomItemClickListener listener) {
        subjects = getDataAdapter;
        this. context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_homepage, parent, false);

        final ViewHolder mViewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getLayoutPosition());
            }
        });
        return mViewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder1=holder;
        mPosition=position;
        getDataAdapter1 =  subjects.get(position);
        if(getDataAdapter1!=null) {

            holder.text.setText(getDataAdapter1.getName());



        }



    }

    @Override
    public int getItemCount() {

        return subjects.size();
    }


        public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ImageView image ;
        public CardView transactions;

        public Button print;


        public ViewHolder(View itemView) {

            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.adapter_icon) ;
            text = (TextView) itemView.findViewById(R.id.inmatename4) ;
            transactions = (CardView) itemView.findViewById(R.id.transactions) ;
           // transactions.setOnClickListener(this);

        }
        public void positionAction(View view) {
            int position = (int) view.getTag();
            System.out.println("pos "+position);

        }
    }


}
