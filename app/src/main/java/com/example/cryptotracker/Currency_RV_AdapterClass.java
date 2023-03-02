package com.example.cryptotracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Currency_RV_AdapterClass extends RecyclerView.Adapter<Currency_RV_AdapterClass.Viewholder> {
        private ArrayList<CurrencyRVmodel> currencyRVmodelArrayList;
        private Context context;
        private static DecimalFormat df2 =new DecimalFormat("#.###");

        public Currency_RV_AdapterClass(ArrayList<CurrencyRVmodel> currencyRVmodelArrayList, Context context) {
                this.currencyRVmodelArrayList = currencyRVmodelArrayList;
                this.context = context;
        }

        public void filterList(ArrayList<CurrencyRVmodel> filterList){
                currencyRVmodelArrayList=filterList;
                notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Currency_RV_AdapterClass.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(context).inflate(R.layout.currencyrvitem,parent,false);
                return new Currency_RV_AdapterClass.Viewholder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Currency_RV_AdapterClass.Viewholder holder, int position) {
                CurrencyRVmodel currencyRVmodel=currencyRVmodelArrayList.get(position);
                holder.name.setText(currencyRVmodel.getName());
                holder.symbol.setText(currencyRVmodel.getSymbol());
                holder.price.setText("$"+df2.format(currencyRVmodel.getPrice()));
        }

        @Override
        public int getItemCount() {
                return currencyRVmodelArrayList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
                private TextView name,symbol,price;
                public Viewholder(@NonNull View itemView) {
                        super(itemView);
                        name=itemView.findViewById(R.id.name);
                        symbol=itemView.findViewById(R.id.symbol);
                        price=itemView.findViewById(R.id.price);
                }
        }
}
