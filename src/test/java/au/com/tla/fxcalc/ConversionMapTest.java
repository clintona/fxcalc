package au.com.tla.fxcalc;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ConversionMapTest extends TestBase {

    private ConversionMap map;

    @Before
    public void setUp() throws Exception {
        this.map = new ConversionMap();
    }

    @Test
    public void testAdd() {
        map.add(new Conversion(AUD, USD, Conversion.DIRECT));
        assertEquals(1, map.getConversions(AUD).size());
    }

    @Test
    public void testGetConversionsEmpty() {
        Set<Conversion> results = map.getConversions(USD);
        assertEquals(0, results.size());
    }

    @Test
    public void testGetSuccess() {
        // uses a multiple map to test we get the correct entry
        Conversion conv = new Conversion(AUD, USD, Conversion.DIRECT);
        Conversion conv2 = new Conversion(AUD, AUD, Conversion.ONE_TO_ONE);
        map.add(conv);
        map.add(conv2);
        assertEquals(conv, map.get(AUD, USD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUninitialised() {
        map.get(USD, AUD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNoMapping() {
        // populated case
        Conversion conv = new Conversion(AUD, USD, Conversion.DIRECT);
        Conversion conv2 = new Conversion(AUD, AUD, Conversion.ONE_TO_ONE);
        map.add(conv);
        map.add(conv2);
        map.get(USD, AUD); // only AUD keys are in the map
    }
}
