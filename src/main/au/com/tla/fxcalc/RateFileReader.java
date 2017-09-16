package au.com.tla.fxcalc;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * A class to read a file of Rates and return a List.
 * The file format is expected to be lines of:
 * <BASE><TERM>=<RATE>
 * or
 * <BASE>/<TERM>=<RATE>
 * 
 * @author Clinton
 *
 */
public class RateFileReader {

    // As per Joshua Bloch "Effective Java"
    // Item 16 - "Favor Composition over Inheritance"
    private CurrencyParser currencyParser = new CurrencyParser();

    /**
     * Set the CurrencyParser used for this instance.
     * 
     * @param CurrencyParser currencyParser
     */
    public void setCurrencyParser(CurrencyParser parser) {
        this.currencyParser = parser;
    }

    /**
     * Read the rate file and return a List of Rate objects.
     * 
     * @param filename
     * @return a List of Rate objects.
     */
    public List<Rate> parseFile(String filename) throws Exception {
        List<Rate> rates = new ArrayList<Rate>();

        Path path = FileSystems.getDefault().getPath(filename);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        for (String line : lines) {
            Rate rate = parseLine(line);
            rates.add(rate);
        }
        return rates;
    }

    protected Rate parseLine(String line) {
        // get the rate multiplier after the = sign
        String[] tokens = line.split("=");
        if (tokens.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid line '" + line + "' unknown rate multiplier");
        }
        String multiplier = tokens[1].trim();
        // parse the Currency part (first token before =)
        Currency[] currencies = currencyParser.parseCurrencies(tokens[0]);

        return new Rate(currencies[0], currencies[1], multiplier);
    }

}
