/**
 * @author imorochi
 * @since 03-08-2019
 */
public class NumerosRomanos {

    public String convertirRomanos(int numeroNatural) {

        switch (numeroNatural){
            case 4:
                return "IV";
            case 9:
                return "IX";
        }

        if (numeroNatural <= 3)
            return sumarI(1,numeroNatural,"");

        if (numeroNatural <= 8)
            return sumarI(6,numeroNatural,"V");

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
}
