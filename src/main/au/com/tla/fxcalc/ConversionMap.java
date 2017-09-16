package au.com.tla.fxcalc;

import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class to store the "cross via matrix" - a table storing how to perform currency conversions
 * between Currencies.
 * 
 * @author Clinton
 *
 */
public class ConversionMap {

    private Map<Currency, Set<Conversion>> conversionMap;

    public ConversionMap() {
        this.conversionMap = new HashMap<Currency, Set<Conversion>>();
    }

    /**
     * Add a currency Conversion to this Conversion map, using the base Currency as a Map key.
     * 
     * @param conversion the target Conversion
     */
    public void add(Conversion conversion) {
        // fetch or create the List of Conversions for this Currency
        Set<Conversion> conversionList = conversionMap.get(conversion.base);
        if (conversionList == null) {
            conversionList = new HashSet<Conversion>();
        }

        conversionList.add(conversion); // ignores duplicates
        conversionMap.put(conversion.base, conversionList);
    }

    /**
     * Return the List of Currency Conversions for this Currency, else an empty List.
     * 
     * @param key Currency to convert
     * @return the List of Currency Conversions for this Currency, else an empty List.
     */
    public Set<Conversion> getConversions(Currency key) {
        Set<Conversion> conversions = this.conversionMap.get(key);
        return conversions == null ? new HashSet<Conversion>(0) : conversions;
    }

    /**
     * Return the matching Conversion for the given base and target Currency.
     * 
     * @param base
     * @param target
     * @return the matching Conversion for the given base and target.
     * @throws IllegalArgumentException if a matching Conversion is not found
     */
    public Conversion get(Currency base, Currency target) {
        Conversion result = null;
        for (Conversion conv : getConversions(base)) {
            if (conv.target == target) {
                result = conv;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Unable to convert " + base + "/" + target);
        }
        return result;
    }
}
