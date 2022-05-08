package models;

import exceptions.DineroInsuficiente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Isaias", new BigDecimal("1000.1234"));
        String esperado = "Isaias";
        String actual = cuenta.getPersona();
        assertNotNull(actual);
        assertEquals(esperado, actual);
        assertTrue(actual.equals("Isaias"));
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        /*  0 : if value of this BigDecimal is equal to that of BigDecimal object passed as parameter.
         *  1 : if value of this BigDecimal is greater than that of BigDecimal object passed as parameter.
         * -1 : if value of this BigDecimal is less than that of BigDecimal object passed as parameter.
         * */
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuentaTwo = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        assertEquals(cuentaOne, cuentaTwo);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficiente.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuenta() {
        Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuentaTwo = new Cuenta("John Doe", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuentaOne, cuentaTwo, new BigDecimal(500));
        assertEquals("1000.8989", cuentaTwo.getSaldo().toPlainString());
        assertEquals("3000", cuentaOne.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuentaTwo = new Cuenta("Isaias", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuentaOne);
        banco.addCuenta(cuentaTwo);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuentaOne, cuentaTwo, new BigDecimal(500));

        assertAll(
                () -> assertEquals("1000.8989", cuentaTwo.getSaldo().toPlainString()),
                () -> assertEquals("3000", cuentaOne.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Banco del Estado", cuentaOne.getBanco().getNombre()),
                () -> assertEquals("Isaias", banco.getCuentas()
                        .stream()
                        .filter(cuenta -> cuenta.getPersona().equalsIgnoreCase("Isaias"))
                        .findFirst()
                        .get()
                        .getPersona()),
                () -> assertTrue(banco.getCuentas()
                        .stream()
                        .filter(cuenta -> cuenta.getPersona().equals("Isaias"))
                        .findFirst().isPresent()),

                () -> assertTrue(banco.getCuentas()
                        .stream()
                        .anyMatch(cuenta -> cuenta.getPersona().equals("Isaias")))
        );

    }

}