package au.com.tla.fxcalc;

import java.util.Currency;

/**
 * Specifies the type of conversion to use to convert to the given ISO 4217 Currency.
 * ie:
 * <ul>
 * <li>ONE_TO_ONE: the target amount is identical to the base amount
 * <li>DIRECT: perform a direct lookup of the rate in the rates table
 * <li>INVERSE: lookup the inverse rate in the rates table
 * <li>currency_code: a 3 digit ISO 4217 currency code, signifying the amount must be converted to
 * this intermediate currency first
 * </ul>
 * 
 * This is a data only class; see https://en.wikipedia.org/wiki/Passive_data_structure
 * 
 * @author Clinton
 *
 */
public class Conversion {

    public static final String ONE_TO_ONE = "1:1";
    public static final String DIRECT = "D";
    public static final String INVERSE = "Inv";

    Currency base;
    Currency target;
    String type;

    public Conversion(Currency base, Currency target, String type) {
        this.base = base;
        this.target = target;
        this.type = type;
    }
}
