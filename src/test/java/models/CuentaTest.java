package models;

import exceptions.DineroInsuficiente;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void beforeEach() {
        System.out.println("Iniciando el metodo de prueba.");
        this.cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
    }

    @AfterEach
    void afterEach() {
        System.out.println("Finalizando el metodo de prueba.");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test.");
    }

    @Test
    @DisplayName("probando el nombre de la cuenta")
    void testNombreCuenta() {
        String esperado = "Isaias";
        String actual = cuenta.getPersona();
        /*EL uso de lambda ayuda a no utilizar muchos recurso por utilizar string planos, ya que estos solo se crean si la prueba falla*/
        assertNotNull(actual, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, actual, () -> "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado + " sin embargo fue " + actual);
        assertTrue(actual.equals("Isaias"), () -> "nombre cuenta esperado debe ser igual al actual");
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado.")
    void testSaldoCuenta() {
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
    @DisplayName("prueba referencias sean iguales con el metodo equals. \uD83D\uDE0E ")
    void testReferenciaCuenta() {
        Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuentaTwo = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        assertEquals(cuentaOne, cuentaTwo);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
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
    @Disabled //permite ignorar esta implementacion de prueba, y util para la documentacion.
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancoCuentas() {
        // forzar el error
        fail();

        Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuentaTwo = new Cuenta("Isaias", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuentaOne);
        banco.addCuenta(cuentaTwo);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuentaOne, cuentaTwo, new BigDecimal(500));

        assertAll(
                () -> assertEquals("1000.8989", cuentaTwo.getSaldo().toPlainString(), () -> "el valor del saldo de la cuenta 2 no es el esperado."),
                () -> assertEquals("3000", cuentaOne.getSaldo().toPlainString(), () -> "el valor del saldo de la cuenta 1 no es el esperado."),
                () -> assertEquals(2, banco.getCuentas().size(), () -> "el banco no tiene las cuentas esperadas."),
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