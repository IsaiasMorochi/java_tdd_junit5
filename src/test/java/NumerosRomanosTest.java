import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author imorochi
 * @since 03-08-2019
 */
public class NumerosRomanosTest {

    private NumerosRomanos numerosRomanos = new NumerosRomanos();

    @Test
    public void pasar1Aromanos(){
        String romano = numerosRomanos.convertirRomanos(1);
        assertEquals(romano, "I");
    }

    @Test
    public void pasar2Aromanos(){
        String romano = numerosRomanos.convertirRomanos(2);
        assertEquals(romano, "II");
    }

    @Test
    public void pasar3Aromanos(){
        String romano = numerosRomanos.convertirRomanos(3);
        assertEquals(romano, "III");
    }

    @Test
    public void pasar4Aromanos(){
        String romano = numerosRomanos.convertirRomanos(4);
        assertEquals(romano, "IV");
    }

    @Test
    public void pasar5Aromanos(){
        String romano = numerosRomanos.convertirRomanos(5);
        assertEquals(romano, "V");
    }

    @Test
    public void pasar6Aromanos(){
        String romano = numerosRomanos.convertirRomanos(6);
        assertEquals(romano, "VI");
    }

    @Test
    public void pasar7Aromanos(){
        String romano = numerosRomanos.convertirRomanos(7);
        assertEquals(romano, "VII");
    }

    @Test
    public void pasar8Aromanos(){
        String romano = numerosRomanos.convertirRomanos(8);
        assertEquals(romano, "VIII");
    }

    @Test
    public void pasar9Aromanos(){
        String romano = numerosRomanos.convertirRomanos(9);
        assertEquals(romano, "IX");
    }
}
