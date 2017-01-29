package com.tdvoice.tdvoice;

import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.*;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TDVoice extends Activity {
    Accounting Chequing = new Accounting(10000, 0,  "Chequing Account");
    Accounting Savings = new Accounting(3000, 1.5, "Savings Account");
    Accounting TFSA = new Accounting(4500, 2.0, "Tax Free Savings Account");

    Bills Visa = new Bills(123, "Visa");
    Bills Hydro = new Bills(200, "Hydro");
    Bills Rent = new Bills(600, "Rent");
    Bills Heating = new Bills(45, "Heating");
    Bills Internet = new Bills(79, "Internet");
    Bills Phone = new Bills(35, "Phone");
    private static JSONObject universe;
    //Bills[] Billist = new Bills[6];
    private static ArrayList<Bills> Billist = new ArrayList<Bills>();





    private static double Bbalance = 0;
    private static double Iinterest = 0;
    private static String outputText= "";
    private static Accounting o;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static Accounting getAccount(){
        return o;
    }
    public static ArrayList<Bills> returnList(){
        return Billist;
    }

    public static JSONObject getStock(){
        return universe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Billist.add(Visa);
        Billist.add(Hydro);
        Billist.add(Rent);
        Billist.add(Heating);
        Billist.add(Internet);
        Billist.add(Phone);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdvoice);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    RequestQueue queue = Volley.newRequestQueue(this);
                    String query = replaceWhitespace(result.get(0));
                    //String fixedQuery = query.replaceAll("[$,]","");
                    String url = "https://api.api.ai/v1/query?query=" + query + "&lang=en&sessionId=1234567890";

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    txtSpeechInput.setText(parseResult(response));

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            txtSpeechInput.setText(error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Authorization", "Bearer e698e893bf9a4bca82e1aa74d7ada583");
                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private String parseResult(String string) {
        try {
            JSONObject responseObj = new JSONObject(string);
            JSONObject resultObj = responseObj.getJSONObject("result");
            String action = resultObj.getString("action");

            switch(action) {
                case "getBalance":
                    JSONObject parametersObj = resultObj.getJSONObject("parameters");
                    String account = parametersObj.getString("accounts");
                    String xyz = getBalance(account);
                    return "";
                case "BillList" :
                    Intent myyIntent = new Intent(TDVoice.this,billlist.class);
                    TDVoice.this.startActivity(myyIntent);
                    return "";
                case "LocateNearestBranch":
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=TD+Canada"));
                    startActivity(intent);

                case "BillPayment":
                    JSONObject parObj = resultObj.getJSONObject("parameters");
                    String bills = parObj.getString("Bills");
                    String accountParameter = parObj.getString("accounts");
                    if (accountParameter.equals("") || accountParameter.equals(" ")) {
                        accountParameter = "chequing account";
                    }
                    double x = 0;
                    switch (bills) {
                        case "VISA":
                            x= Visa.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                        case "Hydro":
                            x= Hydro.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                        case "Rent":
                            x= Rent.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                        case "Heating":
                            x= Heating.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                        case "Internet":
                            x= Internet.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                        case "Phone":
                            x= Phone.removeBalance();
                            switch (accountParameter) {
                                case "savings account":
                                    Savings.subtractBalance(x);
                                    break;
                                case "chequing account":
                                    Chequing.subtractBalance(x);
                                    break;
                                case "tfsa":
                                    TFSA.subtractBalance(x);
                                    break;
                            }
                            break;
                    }
                    return "Payed off " + bills + " balance of $" + x +" from " + accountParameter;

                case "BalanceCheck":
                    return "Your overall balance is $-10.00.\nUh-oh! You're in the red!";

                case "stock":
                    final String stockName = resultObj.getJSONObject("parameters").getString("last-name");
                    final String tickerSymbol = new SymbolTickerMap().getTickerSymbol(stockName);

                    String endpoint = "http://finance.google.com/finance/info?client=ig&q=" + tickerSymbol;

                    RequestQueue stockPriceQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    txtSpeechInput.setText("x");
                                    try {
                                        JSONObject json = new JSONObject(response);
                                        universe = new JSONObject(response);
                                        Intent myyIntent = new Intent(TDVoice.this,Stocks.class);
                                        TDVoice.this.startActivity(myyIntent);
                                        String currentPrice = json.getString("l_cur");

                                        txtSpeechInput.setText(currentPrice);

                                    } catch(Exception e) {}

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            txtSpeechInput.setText("That didn't work!");
                        }
                    });

                    stockPriceQueue.add(stringRequest);
                    break;
                case "transfer":
                    JSONObject paraObj = resultObj.getJSONObject("parameters");
                    String accounts = paraObj.getString("accounts");
                    String accounts1 = paraObj.getString("accounts1");
                    String value = paraObj.getString("number-integer");
                    double value1 = Double.parseDouble(value)*1000;

                    switch (accounts) {
                        case "chequing account":
                            Chequing.subtractBalance(value1);
                            break;
                        case "savings account":
                            Savings.subtractBalance(value1);
                            break;
                        case "tfsa":
                            TFSA.subtractBalance(value1);
                            break;
                    }

                    switch (accounts1) {
                        case "chequing account":
                            Chequing.addBalance(value1);
                            break;
                        case "savings account":
                            Savings.addBalance(value1);
                            break;
                        case "tfsa":
                            TFSA.addBalance(value1);
                            break;
                    }
                    return "Succesfully transferred " + value1 + " from " + accounts + " to " + accounts1;
                default:
                    return "I'm sorry, I can't handle that request.";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("TDVoice Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private String replaceWhitespace(String input) {
        char[] chars = input.toCharArray();

        for (char c : chars) {
            if (c == ' ') c = '+';
        }

        return new String(chars);
    }

    private String getBalance(String account) {
        switch(account) {
            case "chequing account":
                Intent myIntent = new Intent(TDVoice.this,account.class);
                TDVoice.this.startActivity(myIntent);
                this.o = new Accounting(Chequing);


                return "$5000.25";
            case "savings account":
                Intent myyIntent = new Intent(TDVoice.this,account.class);
                TDVoice.this.startActivity(myyIntent);
                o= new Accounting(Savings);

                return "$10.50";
            case "tfsa":
                Intent myyyIntent = new Intent(TDVoice.this,account.class);
                TDVoice.this.startActivity(myyyIntent);
                o= new Accounting(TFSA);
                return "$0.0";

        }

        return "";
    }

}