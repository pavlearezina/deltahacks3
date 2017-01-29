package com.tdvoice.tdvoice;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class account extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Accounting v =TDVoice.getAccount();

        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        String xy = v.getType();
        textView5.setText(v.getType());
        textView6.setText("$" + v.getBalance());
        textView7.setText("Interest " + v.getInterest()+"%");
    }

}
