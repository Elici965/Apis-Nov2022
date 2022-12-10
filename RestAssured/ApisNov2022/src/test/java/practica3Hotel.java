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

public class practica3Hotel {

    static  private String url_base = "restful-booker.herokuapp.com";

    @Test
    @Order(1)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Ping.")
    public void HealthCheck_201(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .get("/ping");

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

        //Validar que el response contenga algunos datos.
        assertTrue(body_response.contains("Created"));
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Test case: Obtener Token.")
    public void obtenerToken_200(){
        RestAssured.baseURI = String.format("https://%s",url_base);
        String body_request = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .headers("Content-Type","application/json")
                .body(body_request)
                .post("/auth");

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
        assertTrue(tiempo < 2000);

        //Validar que el response contenga algunos datos.
        assertTrue(body_response.contains("token"));
    }
}