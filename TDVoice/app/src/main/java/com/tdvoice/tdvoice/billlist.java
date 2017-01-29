package com.tdvoice.tdvoice;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

public class billlist extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billlist);
        ArrayList<Bills> x = TDVoice.returnList();

        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        TextView textView10 = (TextView) findViewById(R.id.textView10);

        textView3.setText(x.get(0).getType()+ "   $" + x.get(0).getBalance());
        textView4.setText(x.get(1).getType()+ "   $" + x.get(1).getBalance());
        textView8.setText(x.get(2).getType()+ "   $" + x.get(2).getBalance());
        textView9.setText(x.get(3).getType()+ "   $" + x.get(3).getBalance());
        textView11.setText(x.get(4).getType()+ "   $" + x.get(4).getBalance());
        textView10.setText(x.get(5).getType()+ "   $" + x.get(5).getBalance());

    }

}
