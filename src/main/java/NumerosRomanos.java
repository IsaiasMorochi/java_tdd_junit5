import java.util.Arrays;
import java.util.List;

/**
 * @author imorochi
 */
public class NumerosRomanos {

    private static final List<String> simbolosRomanos = Arrays.asList("I", "V", "X", "L", "C", "D", "M");

    public String convertirRomanos(Integer numeroNatural) {

        char[] numerosChart = numeroNatural.toString().toCharArray();

        int inc = 0;
        String resultado = "";
        for (int i = numerosChart.length - 1; i >= 0; i--) {
            String romano = generico(Character.getNumericValue(numerosChart[i]),0 + inc,1 + inc,2 + inc);
            resultado = romano + resultado;
            inc += 2;
        }
        return resultado;
    }

    //refactor
    private String generico(int decena, int x, int y, int z) {
        switch (decena){
            case 4:
                return simbolosRomanos.get(x) + simbolosRomanos.get(y);
            case 9:
                return simbolosRomanos.get(x) + simbolosRomanos.get(z);
        }
        if (decena <= 3)
            return sumar(1, decena,"", simbolosRomanos.get(x));
        if (decena <= 8)
            return sumar(6, decena,simbolosRomanos.get(y), simbolosRomanos.get(x));
        return null;
    }

    //refactor
    private String sumar(int inicioCenta, int numeroNatural, String numeroHermano, String incRomano){
        for (int i = inicioCenta; i <= numeroNatural; i++){
            numeroHermano += incRomano;
        }
        return numeroHermano;
    }
}
