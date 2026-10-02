package cliente;

import Cliente.Cliente;
import Cliente.RegistroCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//como trabajo con maping hago lo sgte:
@DisplayName("Hu-01: Registro de clientes")
class RegistroClienteTest {
    private RegistroCliente registro; //variable que importa registro de otro lado

    @BeforeEach
    void inicializarRegistro() {
        registro = new RegistroCliente(); //la prueba empieza vacia luego aislamiento
    }

    //CA1: Nombre no puede estar vacio
    @Nested
    @DisplayName("CA1: Validacion nombre")
    class ValidacionNombre {
        @ParameterizedTest(name = "rechaza nombre invalido: \"{0}\"")
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"}) //eventos
        @DisplayName("rechaza cadenas vacias o solo con espacios en blanco")
        void rechazarNombresEnBlanco(String nombreInvalido) {
            //Act+Assert
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cliente(nombreInvalido, "ana@example.com", "12345678"));

            assertTrue(ex.getMessage().toLowerCase().contains("nombre"), "El mensje de error debe mencionar el campo nombre");

        }

        @Test
        @DisplayName("rechaza nombre null")
        void rechazarNombreNull() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Cliente(null, "ana@example.com", "12345678"));
        }
    }

    //email debe contener @
    @Nested
    @DisplayName("CA2: Validacion email")
    class ValidacionEmail{
        @ParameterizedTest(name ="rechaza email invalido: \"{0}\"")
        @ValueSource(strings = {"ana.example.com", "ana", "", "sin-arroba.pe"})
        @DisplayName("rechaza emails sin @")
        void rechazarEmailsSinArroba(String emailInvalido){
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", emailInvalido, "12345678"));
        }
        @Test
        @DisplayName("rechaza email null")
        void rechazarEmailNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", null, "12345678"));
        }

        @Test
        @DisplayName("acepta un email con @")
        void aceptarEmailConArroba(){
            Cliente c = new Cliente("Ana", "ana@example.com", "12345679");

            //assert
            assertEquals("ana@example.com", c.email());
        }
    }
    //CA3: DNI debe tener 8 caracteres
    @Nested
    @DisplayName("CA3: Validacion DNI")
    class ValidacionDni{
        @ParameterizedTest(name ="rechaza DNI invalido: \"{0}\"")
        @ValueSource(strings = {"123456789", "1234567", "", "12345 678", "ABCDEFGH"})
        @DisplayName("rechaza DNI'S invalidos")
        void rechazarDniInvalido(String dniInvalido){
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", "Ana@example.com", dniInvalido));
        }

        @Test
        @DisplayName("Rechaza DNI null")
        void rechazarDniNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", "Ana@example.com", null));
        }

        @Test
        @DisplayName("acepta DNI de 8 digitos")
        void aceptarDniOchoDigitos(){
            Cliente c = new Cliente("Ana", "ana@example.com", "12345679");
            assertEquals("12345679", c.dni());
        }

    @Nested
    @DisplayName("CA4: unicidad del DNI")
    class unicidadDNI{
        @Test
        @DisplayName("Rechaza el segundo cliente con mismo DNI y no altera el registro")
        void rechazaDniDuplicado(){
                registro.registrarCliente(new Cliente("Ana", "ana@example.com", "12345679"));

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                ()->registro.registrarCliente(new Cliente("Luis", "luis@example.com", "12345679") ));

                assertAll("registro tras intento duplicado",
                        ()->assertTrue(ex.getMessage().contains("12345679"), "el mensaje debe incluir el DNI conflictivo"),
                        ()->assertEquals(1,registro.cantidad(), "no debe haberse agregado el segundo cliente"),
                        ()->assertEquals("Ana",registro.buscarPorDNI("12345679").orElseThrow().nombre(),
                                "el cliente original debe permanecer intacto")
                );
            }

    }
    }
    //Caso 5
    @Nested
    @DisplayName("CA5: consulta por DNI")
    class ConsultaDNI{
        @Test
        @DisplayName("registra cliente valido y recupera DNI")
        void registrarClienteValiedoYRecuperaDNI(){
            //arrange
            Cliente ana = new Cliente("Ana Rosales", "ana@example.com", "12345678");
            //act
            registro.registrarCliente(ana);
            Optional<Cliente> encontrado= registro.buscarPorDNI("12345678");
            //Assert
            assertAll("cliente recuperado",
                    ()->assertEquals(1,registro.cantidad()),
                    ()->assertTrue(encontrado.isPresent()),
                    ()->assertEquals(ana,encontrado.get(), "Los registros con los mimos datos deben ser iguales")
                    );
        }
        @Test
        @DisplayName("la consulta por un DNI inexistente devuelve Optional vacio")
        void consultaDNIInexistente(){
            //act
            Optional<Cliente> resultado = registro.buscarPorDNI("99999999");
            //assert
            assertFalse(resultado.isPresent(), "no debe haber cliente con un DNI nunca registrado");
        }
    }
}


