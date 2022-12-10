import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class practica2Usuarios {

    static  private String url_base = "reqres.in/api";

    @Test
    @Order(1)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Obtener lista de usuarios.")
    public void obtenerListaUsuario_200(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("page","2")
                .get("/users");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //Validar body no es nulo.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000);

        //Validar que el response contenga algunos datos.

        assertTrue(body_response.contains("email"));
        assertTrue(body_response.contains("first_name"));
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Obtener usuario con id.")
    public void obtenerUsuarioConId_200(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .get("/users/2");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //Validar body no es nulo.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000);

        //Validar que el response contenga algunos datos.
        assertTrue(body_response.contains("email"));
        assertTrue(body_response.contains("first_name"));
    }

    @Test
    @Order(3)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Usuario no existente.")
    public void leerUsuarioNoExistente_404(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .get("/users/1212");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(404, response.getStatusCode());

        //Validar body no es nulo.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        //Validar que el response no contenga algunos datos.
        assertFalse(body_response.contains("email"));
        assertFalse(body_response.contains("first_name"));
    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test case: Crear un usuario.")
    public void crearUsuario_201(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        String body_request = "{\n" +
                "    \"name\": \"Isabel\",\n" +
                "    \"job\": \"queen\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .body(body_request)
                .post("/users");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(201, response.getStatusCode());

        //Validar body no es nulo.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar que el response no contenga algunos datos.
        assertTrue(body_response.contains("id"));
        assertTrue(body_response.contains("createdAt"));
    }

    @Test
    @Order(5)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test case: Actualizar un usuario con id.")
    public void actualizarUsuario_201(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        String body_request = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .body(body_request)
                .put("/users/2");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //Validar body no es nulo.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000);

        //Validar que el response contenga algunos datos.
        assertTrue(body_response.contains("updatedAt"));
    }

    @Test
    @Order(6)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test case: Eliminar un usuario con id.")
    public void eliminarUsuario_204(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        String body_request = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .body(body_request)
                .delete("/users/2");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(204, response.getStatusCode());

        //Validar body response está vacio.
        assertTrue(body_response.isEmpty());

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000);

        //Validar el headers response contenga Content-Length=0.
        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("Content-Length=0"));
    }

    @Test
    @Order(6)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Registrar un usuario.")
    public void registrarUsuario_200(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        String body_request = "{\n" +
                "    \"email\": \"lindsay.ferguson@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .headers("Content-Type","application/json")
                .headers("Accept", "*/*")
                .body(body_request)
                .post("/register");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Validar código de respuesta.
        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //Validar body response está vacio.
        assertNotNull(body_response);

        //Validar tiempo de respuesta.
        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000);

        //Validar que el response contenga algunos datos.
        assertTrue(body_response.contains("id"));
        assertTrue(body_response.contains("token"));
    }
}
