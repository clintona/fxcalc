package au.com.tla.fxcalc;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

/**
 * A stand-alone Java program to perform currency conversions.
 * 
 * @author Clinton
 *
 */
public class FxCalc {

    // You can override the default configuration filenames and locations using eg:
    // java -Drates.file=/mypath/to/rates.file
    protected static final String KEY_RATES_FILE = "rates.file";
    protected static final String KEY_CONVERSION_FILE = "conversions.file";
    protected static final String KEY_ROUNDING_MODE = "rounding.mode";

    // Default filename values for configuration files
    private static final String DEFAULT_RATES_FILE = "rates.txt";
    private static final String DEFAULT_CONVERSION_FILE = "conversions.txt";

    /**
     * FxCalc main entry point.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            usage();
            return;
        }
        try {
            Currency base = getCurrency(args[0]);
            BigDecimal amount = getAmount(args[1]);
            Currency target = getCurrency(args[3]);

            CurrencyConverter converter = init();

            BigDecimal result = converter.convert(base, target, amount);

            System.out.println(base + " " + amount + " = " + target + " " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            usage();
        }
    }

    private static CurrencyConverter init() throws Exception {

        CurrencyConverter converter = new CurrencyConverter();
        converter.setRateList(getRates());
        converter.setConversionMap(getConversions());
        RoundingMode mode = getRoundingMode();
        if (mode != null) {
            converter.setRoundingMode(mode);
        }
        return converter;
    }

    private static List<Rate> getRates() throws Exception {
        String ratesFilename = getPropertyOrDefault(KEY_RATES_FILE, DEFAULT_RATES_FILE);
        RateFileReader rateReader = new RateFileReader();
        try {
            return rateReader.parseFile(ratesFilename);
        } catch (IOException e) {
            throw new IOException("Unable to find " + ratesFilename);
        }
    }

    private static ConversionMap getConversions() throws Exception {
        String conversionFilename = getPropertyOrDefault(KEY_CONVERSION_FILE,
                DEFAULT_CONVERSION_FILE);
        ConversionMapFileReader conversionReader = new ConversionMapFileReader();
        try {
            return conversionReader.parseFile(conversionFilename);
        } catch (IOException e) {
            throw new IOException("Unable to find " + conversionFilename);
        }

    }

    /**
     * Return the RoundingMode System Property 'KEY_ROUNDING_MODE', else null
     * 
     * @return the RoundingMode System Property 'KEY_ROUNDING_MODE', else null
     */
    private static RoundingMode getRoundingMode() {
        RoundingMode mode = null;
        String str = System.getProperty(KEY_ROUNDING_MODE);
        if (str != null) {
            try {
                mode = RoundingMode.valueOf(str);
            } catch (Exception ignored) {
                System.err.println("Unknown RoundingMode " + str + " using default");
            }
        }
        return mode;
    }

    private static String getPropertyOrDefault(String key, String defaultValue) {
        String value = System.getProperty(key);
        return value == null ? defaultValue : value;
    }

    private static BigDecimal getAmount(String str) {
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            throw new NumberFormatException("Invalid amount '" + str + "'");
        }
    }

    /**
     * Return a Currency instance for the given ISO4217 3 letter code.
     * Override any Exception with a more useful description.
     * 
     * @param str
     * @return a Currency instance for the given ISO4217 3 letter code.
     * @throws IllegalArgumentException
     */
    private static Currency getCurrency(String str) {
        try {
            return Currency.getInstance(str);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Currency code '" + str + "'");
        }
    }

    /**
     * Print a usage message to stdout.
     */
    public static void usage() {
        System.out.println("Usage: FxCalc {base} {amount} in {target}");
        System.out.println("Converts the base currency amount to the target currency, where:");
        System.out.println("{base} and {target} are ISO-4217 3 digit codes");
        System.out.println("{amount} is a number");
    }

}
