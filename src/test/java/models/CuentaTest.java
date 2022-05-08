package models;

import exceptions.DineroInsuficiente;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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

    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class CuentaNombreTest {
        @Test
        @DisplayName("el nombre de la cuenta")
        void testNombreCuenta() {
            String esperado = "Isaias";
            String actual = cuenta.getPersona();
            /*EL uso de lambda ayuda a no utilizar muchos recurso por utilizar string planos, ya que estos solo se crean si la prueba falla*/
            assertNotNull(actual, () -> "La cuenta no puede ser nula");
            assertEquals(esperado, actual, () -> "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado + " sin embargo fue " + actual);
            assertTrue(actual.equals("Isaias"), () -> "nombre cuenta esperado debe ser igual al actual");
        }

        @Test
        @DisplayName("el saldo, que no sea null, mayor que cero, valor esperado.")
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
        @DisplayName("referencias sean iguales con el metodo equals. \uD83D\uDE0E ")
        void testReferenciaCuenta() {
            Cuenta cuentaOne = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta cuentaTwo = new Cuenta("John Doe", new BigDecimal("8900.9997"));

            assertEquals(cuentaOne, cuentaTwo);
        }

    }

    @Nested
    class CuentaOperacionesTest {
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

        //@RepeatedTest(3)
        @DisplayName("Probando Debito Cuenta Repetir!")
        @RepeatedTest(value = 3, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
        void testDebitoCuentaRepetir(RepetitionInfo info) {

            // custom para realizar alguna accion en una repeticion especifica
            if (info.getCurrentRepetition() == 3) {
                System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
            }
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

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

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJDK8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void soloJDK11() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_11)
        void testNoJDK11() {
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void showSystemroperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64Bits() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "imorochi")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    class VariableAmbienteTest {
        @Test
        void showVariablesEnviroments() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(String.format("%s = %s", k, v)));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "4")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }
    }


    @Test
    @DisplayName("Test Saldo cuenta dev.")
    void testSaldoCuentaDev() {
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(isDev); //Assumptions permite determinar si se ejecuta o no el test de prueba.
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Test Saldo cuenta dev 2.")
    void testSaldoCuentaDev2() {
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(isDev, () -> { //permite que se ejecute el metodo test, pero solo si se cumple la condicion ingresa a ejecutar el lambda
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

}