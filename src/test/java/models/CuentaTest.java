package models;

import exceptions.DineroInsuficiente;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;
    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test.");
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo, TestReporter testReporter) {
        System.out.println("Iniciando el metodo de prueba.");
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        this.cuenta = new Cuenta("Isaias", new BigDecimal("1000.12345"));
        // log de Junit
        testReporter.publishEntry("Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName() + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void afterEach() {
        System.out.println("Finalizando el metodo de prueba.");
    }

    @Tag("cuenta")
    @Tag("error")
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

    @Tag("cuenta")
    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class CuentaNombreTest {
        @Test
        @DisplayName("el nombre de la cuenta")
        void testNombreCuenta() {
            System.out.println(String.format("Tags -> %s ", testInfo.getTags()));
            if (testInfo.getTags().contains("cuenta")) {
                testReporter.publishEntry("Hacer algo con la etiqueta cuenta.");
            }
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
        @Tag("cuenta")
        @Test
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Test
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Tag("banco")
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

        @Tag("cuenta")
        @Tag("banco")
        @Test
        //@Disabled //permite ignorar esta implementacion de prueba, y util para la documentacion.
        @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
        void testRelacionBancoCuentas() {
            // forzar el error
            //fail();

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

    @Tag("params")
    @Nested
    class PruebasParametrizadasTest {

        //@ValueSource(doubles = {100, 200, 300, 500, 700, 1000.12345}) //corremos riesgo de la perdida de precision
        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(String.format("%s -> %s", index, monto));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100,Isaias,Isaias", "250,200,John,John", "300,300,Mario,Mario", "510,500,Isaias,Isaias", "750,700,John,John", "1000.12345,1000.12345,John,John"})
        void testDebitoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(String.format("%s -> %s", saldo, monto));
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);
            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(String.format("%s -> %s", saldo, monto));
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);
            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

       /* private static List<String> montoList() {
            return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        void testDebitoCuentaMethodSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }*/

    }

    @Nested
    @Tag("timeout")
    class TimeoutTest {
        @Test
        @Timeout(1) //segundos
        void testTimeoutOne() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS) //segundos
        void testTimeoutTwo() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeoutThree() {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(4000);
            } );
        }

    }

}