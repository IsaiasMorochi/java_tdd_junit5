import java.util.Arrays;
import java.util.List;

/**
 * @author imorochi
 */
public class NumerosRomanos {

    private static final List<String> simbolosRomanos = Arrays.asList("I", "V", "X", "L", "C");

    public String convertirRomanos(Integer numeroNatural) {
        char[] numerosChart = numeroNatural.toString().toCharArray();
        if (numerosChart.length >= 2){
            String unidadRomana = generico(Character.getNumericValue(numerosChart[1]),0,1,2);
            String decenaRomana = generico(Character.getNumericValue(numerosChart[0]),2,3,4);
            return decenaRomana + unidadRomana;
        }
        if (numerosChart.length >= 1)
            return generico(Character.getNumericValue(numerosChart[0]), 0, 1, 2);

        return null;
    }

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

    /*
    private String sumarI(int numeroNatural){
        String resultado = "";
        for (int i = 1; i <= numeroNatural; i++){
            resultado += "I";
        }
        return resultado;
    }

    private String sumarIAPartirV(int numeroNatural){
        String resultado = "V";
        for (int i = 6; i <= numeroNatural; i++){
            resultado += "I";
        }
        return resultado;
    }

    private String sumarI(int inicioCenta, int numeroNatural, String numeroHermano){
        for (int i = inicioCenta; i <= numeroNatural; i++){
            numeroHermano += "I";
        }
        return numeroHermano;
    }

    private String sumarX(int inicioCenta, int numeroNatural, String numeroHermano){
        for (int i = inicioCenta; i <= numeroNatural; i++){
            numeroHermano += "X";
        }
        return numeroHermano;
    }

    private String pasarDecena(int decena) {
        switch (decena){
            case 4:
                return "XL";
            case 9:
                return "XC";
        }
        if (decena <= 3)
            return sumar(1, decena,"","X");
        if (decena <= 8)
            return sumar(6, decena,"L","X");
        return null;
    }

    private String pasarUnidad(int unidad) {
        switch (unidad){
            case 4:
                return "IV";
            case 9:
                return "IX";
        }
        if (unidad <= 3)
            return sumar(1,unidad,"","I");
        if (unidad <= 8)
            return sumar(6,unidad,"V","I");
        return null;
    }*/
}
