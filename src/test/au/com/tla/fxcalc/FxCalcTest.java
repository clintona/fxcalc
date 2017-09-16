package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FxCalcTest {

    private ByteArrayOutputStream testOut = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(testOut));
        // setup sys props
        System.setProperty(FxCalc.KEY_RATES_FILE, "src/test/resources/test_rates.txt");
        System.setProperty(FxCalc.KEY_CONVERSION_FILE, "src/test/resources/test_conversions.txt");
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(null);
        // unset config
        System.setProperty(FxCalc.KEY_RATES_FILE, "rates.txt");
        System.setProperty(FxCalc.KEY_CONVERSION_FILE, "conversions.txt");
    }

    @Test
    public void testMain() {
        String[] args = new String[] { "AUD", "1", "in", "USD" };
        FxCalc.main(args);
        assertEquals("AUD 1 = USD 0.84" + System.lineSeparator(), testOut.toString());
    }

    @Test
    public void testNoConversionFile() {
        // unset config
        System.setProperty(FxCalc.KEY_CONVERSION_FILE, "conversions.txt");

        String[] args = new String[] { "AUD", "1", "in", "USD" };
        FxCalc.main(args);
        assertTrue(testOut.toString().startsWith("Unable to find conversions.txt"));
    }

    @Test
    public void testNoRatesFile() {
        // unset config
        System.setProperty(FxCalc.KEY_RATES_FILE, "rates.txt");

        String[] args = new String[] { "AUD", "1", "in", "USD" };
        FxCalc.main(args);
        assertTrue(testOut.toString().startsWith("Unable to find rates.txt"));
    }

    @Test
    public void testBadArgs() {
        String[] args = new String[] { "Not", "Enough", "args" };
        FxCalc.main(args);
        assertTrue(testOut.toString().startsWith("Usage: FxCalc"));
    }

    @Test
    public void testBadAmount() {
        String[] args = new String[] { "AUD", "XXX", "in", "USD" };
        FxCalc.main(args);
        assertTrue(testOut.toString().startsWith("Invalid amount 'XXX'"));
    }

    @Test
    public void testBadCurrency() {
        String[] args = new String[] { "AUD", "1", "in", "XYZ" };
        FxCalc.main(args);
        assertTrue(testOut.toString().startsWith("Invalid Currency code 'XYZ'"));
    }

}
