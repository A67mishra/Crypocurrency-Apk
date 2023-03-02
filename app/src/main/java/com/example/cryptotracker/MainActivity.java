package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText srch;
    RecyclerView rv;
    ProgressBar bar;
    ArrayList<CurrencyRVmodel> currencyRVmodelArrayList;
    Currency_RV_AdapterClass currency_rv_adapterClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        srch=findViewById(R.id.search);
        rv=findViewById(R.id.recyclerview);
        bar=findViewById(R.id.loading);
        currencyRVmodelArrayList=new ArrayList<>();
        currency_rv_adapterClass=new Currency_RV_AdapterClass(currencyRVmodelArrayList,this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(currency_rv_adapterClass);
        getCurrencyData();

        srch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        });
    }

    private  void filterCurrencies(String currency){
        ArrayList<CurrencyRVmodel> filteredList=new ArrayList<>();
        for (CurrencyRVmodel item:currencyRVmodelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Currency Found for searched query.", Toast.LENGTH_SHORT).show();

        }else {
            currency_rv_adapterClass.filterList(filteredList);
        }
    }

    private void getCurrencyData(){
        bar.setVisibility(View.VISIBLE);
        String url ="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                bar.setVisibility(View.GONE);
                try{
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject dataobject=array.getJSONObject(i);
                        String n=dataobject.getString("name");
                        String s=dataobject.getString("symbol");
                        JSONObject qoute=dataobject.getJSONObject("quote");
                        JSONObject USD=qoute.getJSONObject("USD");
                        double p=USD.getDouble("price");
                        currencyRVmodelArrayList.add(new CurrencyRVmodel(n,s,p));
                    }
                    currency_rv_adapterClass.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to extract JSON data..", Toast.LENGTH_SHORT).show();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to fetch data...", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers =new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","8fca86ff-c9a4-4687-9d13-383a3bc586e2");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}