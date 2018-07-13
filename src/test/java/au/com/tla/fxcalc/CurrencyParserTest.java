package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class CurrencyParserTest extends TestBase {

    private CurrencyParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new CurrencyParser();
    }

    @Test
    public void testParseCurrenciesSlash() {
        String line = "AUD/USD";

        Currency[] currencies = parser.parseCurrencies(line);

        assertEquals(AUD, currencies[0]);
        assertEquals(USD, currencies[1]);
    }

    @Test
    public void testParseCurrenciesJoined() {
        String line = "AUDUSD ";

        Currency[] currencies = parser.parseCurrencies(line);

        assertEquals(AUD, currencies[0]);
        assertEquals(USD, currencies[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCurrenciesInvalid() {
        String line = "AUD ";

        parser.parseCurrencies(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCurrenciesUnknownCurrency() {
        String line = "AUDXYZ";

        parser.parseCurrencies(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCurrenciesTooManyTokens() {
        String line = "AUD/XYZ/XXX";

        parser.parseCurrencies(line);
    }
}
