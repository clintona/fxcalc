package au.com.tla.fxcalc;

import java.util.Currency;

/**
 * Parses Currency instances from a String.
 * Eg: "AUDUSD" results in 2 Currency instances, "AUD" and "USD".
 * 
 * @author Clinton
 *
 */
public class CurrencyParser {

    /**
     * Return two java.util.Currency instances from the given String, in an array.
     * 
     * @param currencyTokens A String that is expected to be formatted as either:<br/>
     *        {Currency1}{Currency2} or {Currency1}/{Currency2}
     * 
     * @return two java.util.Currency instances from the given String, in an array.
     * @throws IllegalArgumentException if the Currencies cannot be derived
     */
    public Currency[] parseCurrencies(String currencyTokens) {
        String base;
        String target;
        String[] tokens = currencyTokens.split("/");
        if (tokens.length == 1) { // parse joined format
            base = tokens[0].substring(0, 3);
            target = tokens[0].substring(3).trim();
        } else if (tokens.length == 2) { // parse split format
            base = tokens[0].trim();
            target = tokens[1].trim();
        } else {
            throw new IllegalArgumentException("Invalid currencies '" + currencyTokens + "'");
        }
        Currency[] currencies = new Currency[2];
        currencies[0] = Currency.getInstance(base);
        currencies[1] = Currency.getInstance(target);
        return currencies;
    }
}
