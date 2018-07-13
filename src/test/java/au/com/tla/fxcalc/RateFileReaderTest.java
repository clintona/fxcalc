package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RateFileReaderTest extends TestBase {

    private RateFileReader parser;

    @Before
    public void setUp() throws Exception {
        parser = new RateFileReader();
    }

    @Test
    public void testParseLineSuccessJoined() {
        String line = "AUDUSD=0.8371";

        Rate rate = parser.parseLine(line);

        assertEquals(AUD, rate.base);
        assertEquals(USD, rate.target);
        assertEquals(new BigDecimal("0.8371"), rate.multiplier);
    }

    @Test
    public void testParseLineSuccessSlash() {
        String line = "AUD/USD=0.8371";

        Rate rate = parser.parseLine(line);

        assertEquals(AUD, rate.base);
        assertEquals(USD, rate.target);
        assertEquals(new BigDecimal("0.8371"), rate.multiplier);
    }

    @Test
    public void testParseLineSuccessSpaced() {
        String line = "AUD / USD = 0.8371";

        Rate rate = parser.parseLine(line);

        assertEquals(AUD, rate.base);
        assertEquals(USD, rate.target);
        assertEquals(new BigDecimal("0.8371"), rate.multiplier);
    }

    @Test(expected = NullPointerException.class)
    public void testParseLineNull() {
        String line = null;

        parser.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLineEmpty() {
        String line = "";

        parser.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLineCloseButNoCigar() {
        String line = "HIH/HOH=-0.123";

        parser.parseLine(line);
    }

    @Test
    public void testParseFile() throws Exception {
        List<Rate> rates = parser.parseFile("src/test/resources/test_rates.txt");

        assertEquals(AUD, rates.get(0).base);
        assertEquals(USD, rates.get(0).target);
        assertEquals(new BigDecimal("0.8371"), rates.get(0).multiplier);

        assertEquals(CAD, rates.get(1).base);
        assertEquals(USD, rates.get(1).target);
        assertEquals(new BigDecimal("0.8711"), rates.get(1).multiplier);
    }

    @Test(expected = IOException.class)
    public void testParseFileNotFound() throws Exception {
        parser.parseFile("_I_dont_exist_");
    }
}
