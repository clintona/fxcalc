package au.com.tla.fxcalc;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Specifies the conversion rate (multiplier) to convert from the base Currency to the target
 * Currency.
 * This is a data only class; see https://en.wikipedia.org/wiki/Passive_data_structure
 * 
 * 
 * @author Clinton
 *
 */
public class Rate {
    Currency base;
    Currency target;
    BigDecimal multiplier;

    public Rate(Currency base, Currency target, String rate) {
        this.base = base;
        this.target = target;
        this.multiplier = new BigDecimal(rate);
    }

}
