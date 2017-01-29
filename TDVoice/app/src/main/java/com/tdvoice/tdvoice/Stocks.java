package com.tdvoice.tdvoice;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Stocks extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        JSONObject x = TDVoice.getStock();
        String ticker = "";
        String tick = "";
        String currentPrice = "";
        String percent = "";
        try {
            ticker = x.getString("e");
            tick = x.getString("t");
            currentPrice = x.getString("l_cur");
            percent = x.getString("c");
        }
        catch(Exception e){}


        TextView textView12 = (TextView) findViewById(R.id.textView12);
        TextView textView13 = (TextView) findViewById(R.id.textView13);
        TextView textView14 = (TextView) findViewById(R.id.textView14);
        TextView textView15 = (TextView) findViewById(R.id.textView15);

        textView12.setText(ticker);
        textView13.setText(tick);
        textView14.setText("$ " +currentPrice);
        textView15.setText(percent);

    }

}
