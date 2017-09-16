package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ConversionMapFileReaderTest extends TestBase {

    private ConversionMapFileReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new ConversionMapFileReader();
    }

    @Test
    public void testParseLineSuccessJoined() {
        String line = "AUDUSD=D";

        Conversion conv = reader.parseLine(line);

        assertEquals(AUD, conv.base);
        assertEquals(USD, conv.target);
        assertEquals("D", conv.type);
    }

    @Test
    public void testParseLineSuccessSlash() {
        String line = "AUD/USD=Inv";

        Conversion conv = reader.parseLine(line);

        assertEquals(AUD, conv.base);
        assertEquals(USD, conv.target);
        assertEquals("Inv", conv.type);
    }

    @Test
    public void testParseLineSuccessSpaced() {
        String line = "AUD / USD = 1:1";

        Conversion conv = reader.parseLine(line);

        assertEquals(AUD, conv.base);
        assertEquals(USD, conv.target);
        assertEquals("1:1", conv.type);
    }

    @Test(expected = NullPointerException.class)
    public void testParseLineNull() {
        String line = null;

        reader.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLineEmpty() {
        String line = "";

        reader.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLineCloseButNoCigar() {
        String line = "HIH/HOH=Blah";

        reader.parseLine(line);
    }

    @Test
    public void testParseFileSuccess() throws Exception {
        // conversions test file is expected to house 4 entries:
        // AUDUSD=D AUDAUD=1:1 AUDCAD=USD USDAUD=Inv
        ConversionMap map = reader.parseFile("src/test/resources/test_conversions.txt");

        assertEquals(3, map.getConversions(AUD).size());
        assertEquals(1, map.getConversions(USD).size());
    }

    @Test(expected = IOException.class)
    public void testParseFileNotFound() throws Exception {
        reader.parseFile("_I_dont_exist_");
    }
}
