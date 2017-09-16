package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CurrencyConverterTest extends TestBase {

    private CurrencyConverter converter;

    @Before
    public void setUp() {
        // setup some conversion data
        converter = new CurrencyConverter();
        converter.setConversionMap(buildMatrix());
        converter.setRateList(buildRateList());
    }

    protected ConversionMap buildMatrix() {
        ConversionMap matrix = new ConversionMap();

        matrix.add(new Conversion(AUD, AUD, Conversion.ONE_TO_ONE));
        matrix.add(new Conversion(AUD, USD, Conversion.DIRECT));
        matrix.add(new Conversion(AUD, CAD, "USD"));
        matrix.add(new Conversion(CAD, USD, Conversion.DIRECT));
        matrix.add(new Conversion(USD, CAD, Conversion.INVERSE));
        matrix.add(new Conversion(USD, JPY, Conversion.DIRECT));
        matrix.add(new Conversion(AUD, JPY, "USD"));

        return matrix;
    }

    protected List<Rate> buildRateList() {
        List<Rate> rates = new ArrayList<Rate>();

        rates.add(new Rate(AUD, USD, "0.8371"));
        rates.add(new Rate(CAD, USD, "0.8711"));
        rates.add(new Rate(USD, JPY, "119.95"));

        return rates;
    }

    @Test
    public void testOneToOne() {
        assertEquals(new BigDecimal("1.00"), converter.convert(AUD, AUD, BigDecimal.ONE));
        assertEquals(new BigDecimal("1.00"), converter.convert(AUD, AUD, new BigDecimal("1.0")));
        assertEquals(new BigDecimal("1.00"), converter.convert(AUD, AUD, new BigDecimal("1.00")));
        assertEquals(new BigDecimal("1.00"),
                converter.convert(AUD, AUD, new BigDecimal("1.0000000")));
    }

    @Test
    public void testDirect() {
        assertEquals(new BigDecimal("0.84"), converter.convert(AUD, USD, BigDecimal.ONE));
    }

    @Test
    public void testJPYPrecision() {
        // assumes default rounding mode of ROUND_HALF
        assertEquals(new BigDecimal("120"), converter.convert(USD, JPY, BigDecimal.ONE));
    }

    @Test
    public void testInverse() {
        assertEquals(new BigDecimal("1.15"), converter.convert(USD, CAD, BigDecimal.ONE));
    }

    @Test
    public void testCrossVia() {
        // AUD->CAD is via USD and Inverse
        assertEquals(new BigDecimal("0.96"), converter.convert(AUD, CAD, BigDecimal.ONE));

        // AUD->JPY is via USD. Note rounding
        assertEquals(new BigDecimal("100"), converter.convert(AUD, JPY, BigDecimal.ONE));
    }

    @Test(expected = NullPointerException.class)
    public void testSetMapException() {
        converter.setConversionMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetRatesException() {
        converter.setRateList(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetRoundingModeException() {
        converter.setRoundingMode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRateException() {
        Currency XXX = Currency.getInstance("XXX");
        converter.getRate(AUD, XXX);
    }
}
