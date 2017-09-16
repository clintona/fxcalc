package au.com.tla.fxcalc;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Currency;
import java.util.List;

/**
 * A class to read a file of Conversions and return a Map.
 * The file format is expected to be lines of: <br/>
 * <BASE><TERM>=<RATE> or <BASE>/<TERM>=<RATE>
 * 
 * @author Clinton
 *
 */
public class ConversionMapFileReader {

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
     * Read the conversion file and return a Map of Conversion objects.
     * 
     * @param filename
     * @return a Map of Conversion objects.
     */
    public ConversionMap parseFile(String filename) throws Exception {
        ConversionMap map = new ConversionMap();

        Path path = FileSystems.getDefault().getPath(filename);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        for (String line : lines) {
            Conversion conv = parseLine(line);
            map.add(conv);
        }
        return map;
    }

    protected Conversion parseLine(String line) {
        // get the Conversion type after the = sign
        String[] tokens = line.split("=");
        if (tokens.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid line '" + line + "' unknown conversion type");
        }
        String type = tokens[1].trim();
        // parse the Currency part (first token before =)
        Currency[] currencies = currencyParser.parseCurrencies(tokens[0]);

        return new Conversion(currencies[0], currencies[1], type);
    }

}
