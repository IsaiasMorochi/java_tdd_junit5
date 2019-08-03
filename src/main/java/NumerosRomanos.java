/**
 * @author imorochi
 */
public class NumerosRomanos {

    public String convertirRomanos(Integer numeroNatural) {

        char[] numerosChart = numeroNatural.toString().toCharArray();

        if (numerosChart.length >= 2){
            String unidadRomana = pasarUnidad(Character.getNumericValue(numerosChart[1]));
            String decenaRomana = pasarDecena(Character.getNumericValue(numerosChart[0]));
            return decenaRomana + unidadRomana;
        }

        if (numerosChart.length >= 1)
            return pasarUnidad(Character.getNumericValue(numerosChart[0]));

        return null;
    }

    private String pasarDecena(int decena) {
        switch (decena){
            case 4:
                return "XL";
            case 9:
                return "XC";
        }

        if (decena <= 3)
            return sumarX(1, decena,"");

        if (decena <= 8)
            return sumarX(6, decena,"L");

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
            return sumarI(1,unidad,"");

        if (unidad <= 8)
            return sumarI(6,unidad,"V");

        return null;
    }

//    private String sumarI(int numeroNatural){
//        String resultado = "";
//        for (int i = 1; i <= numeroNatural; i++){
//            resultado += "I";
//        }
//        return resultado;
//    }
//
//    private String sumarIAPartirV(int numeroNatural){
//        String resultado = "V";
//        for (int i = 6; i <= numeroNatural; i++){
//            resultado += "I";
//        }
//        return resultado;
//    }

    //refactor
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
}
