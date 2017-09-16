package au.com.tla.fxcalc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * A class to calculate Currency conversions for a given base Currency amount and target Currency.
 * 
 * @author Clinton
 *
 */
public class CurrencyConverter {

    private ConversionMap conversionMap;
    private List<Rate> rateList;
    private RoundingMode roundingMode;

    public CurrencyConverter() {
        conversionMap = new ConversionMap();
        rateList = new ArrayList<Rate>();
        roundingMode = RoundingMode.HALF_UP;
    }

    public void setConversionMap(ConversionMap conversionMap) {
        if (conversionMap == null) {
            throw new NullPointerException("null conversionMap");
        }
        this.conversionMap = conversionMap;
    }

    public void setRateList(List<Rate> rateList) {
        if (rateList == null) {
            throw new NullPointerException("null rateList");
        }
        this.rateList = rateList;
    }

    /**
     * Set the RoundingMode if needed. RoundingMode defaults to HALF_UP.
     * 
     * @param mode RoundingMode
     */
    public void setRoundingMode(RoundingMode mode) {
        if (mode == null) {
            throw new NullPointerException("RoundingMode cannot be null");
        }
        this.roundingMode = mode;
    }

    /**
     * Convert the baseCurrency amount to the targetCurrency, with rounding.
     * Uses the built-in decimal precision of the ISO 4217 java.util.Currency class.
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param amount
     * @return the baseCurrency amount in the targetCurrency
     */
    public BigDecimal convert(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        BigDecimal result = convertNoRounding(baseCurrency, targetCurrency, amount);
        // only perform rounding at the end otherwise rounding errors occur
        return result.setScale(targetCurrency.getDefaultFractionDigits(), roundingMode);
    }

    /**
     * Convert the baseCurrency amount to the targetCurrency,
     * without rounding (which introduces errors).
     * Uses the built-in decimal precision of the ISO 4217 java.util.Currency class.
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param amount
     * @return the baseCurrency amount in the targetCurrency
     */
    public BigDecimal convertNoRounding(Currency baseCurrency, Currency targetCurrency,
            BigDecimal amount) {
        BigDecimal result = null;

        // Lookup the conversion for the base and target combination
        Conversion conversion = conversionMap.get(baseCurrency, targetCurrency);

        // Implement the conversion algorithm (type)
        if (Conversion.ONE_TO_ONE.equals(conversion.type)) {
            result = amount;
        } else if (Conversion.DIRECT.equals(conversion.type)) {
            result = convertDirect(baseCurrency, targetCurrency, amount);
        } else if (Conversion.INVERSE.equals(conversion.type)) {
            result = convertInverse(baseCurrency, targetCurrency, amount);
        } else { // cross via currency
            result = convertVia(baseCurrency, targetCurrency, Currency.getInstance(conversion.type),
                    amount);
        }
        return result;
    }

    /**
     * Return the matching Rate for the given base and target Currency.
     * 
     * @param base Currency
     * @param target Currency
     * @return the matching Rate for the given base and target Currency.
     * @throws IllegalArgumentException if the Rate cannot be found
     */
    protected Rate getRate(Currency base, Currency target) {
        Rate result = null;
        for (Rate r : rateList) {
            if (r.base == base && r.target == target) {
                result = r;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Unable to find rate for " + base + "/" + target);
        }
        return result;
    }

    /**
     * Return the conversion amount using the Direct lookup method.
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param amount
     * @return the baseCurrency amount in the targetCurrency
     * 
     */
    protected BigDecimal convertDirect(Currency baseCurrency, Currency targetCurrency,
            BigDecimal amount) {
        // Lookup direct rate
        Rate rate = getRate(baseCurrency, targetCurrency);
        return amount.multiply(rate.multiplier);
    }

    /**
     * Return the conversion amount using the Inverse lookup method.
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param amount
     * @return the baseCurrency amount in the targetCurrency
     * 
     */
    protected BigDecimal convertInverse(Currency baseCurrency, Currency targetCurrency,
            BigDecimal amount) {
        Rate rate = getRate(targetCurrency, baseCurrency); // note inverse search terms
        // Using Math.pow(-1) requires a MathContext precision. Use 10 for accuracy.
        BigDecimal inverseRate = rate.multiplier.pow(-1, new MathContext(10));
        return amount.multiply(inverseRate);
    }

    /**
     * Return the conversion amount using the "cross via" method.
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param crossCurrency
     * @param amount
     * @return the baseCurrency amount in the targetCurrency
     * 
     */
    protected BigDecimal convertVia(Currency baseCurrency, Currency targetCurrency,
            Currency crossCurrency, BigDecimal amount) {
        // convert the baseCurrency to the crossCurrency
        BigDecimal crossAmount = convertNoRounding(baseCurrency, crossCurrency, amount);

        // then convert the crossAmount to the targetCurrency
        return convertNoRounding(crossCurrency, targetCurrency, crossAmount);
    }
}
