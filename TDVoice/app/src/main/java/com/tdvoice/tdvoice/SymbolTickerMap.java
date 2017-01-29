package com.tdvoice.tdvoice;

import java.util.HashMap;

/**
 * Created by Pavle on 2017-01-28.
 */

public class SymbolTickerMap {

    final HashMap<String, String> tickerMap = new HashMap<>();

    public SymbolTickerMap() {
        tickerMap.put("Apple", "AAPL");
        tickerMap.put("Microsoft", "MSFT");
        tickerMap.put("TWITTER", "TWTR");
        tickerMap.put("TD", "TD");
        tickerMap.put("TD Bank", "TD");
        tickerMap.put("AMD", "AMD");
        tickerMap.put("Google", "GOOG");
        tickerMap.put("Amazon", "AMZN");
        tickerMap.put("Tesla", "TSLA");
    }

    public String getTickerSymbol(String name) {
        return tickerMap.get(name);
    }

}
