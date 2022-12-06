import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class practicaHotel {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    public void practicaHotel() {
        RestAssured.baseURI = String.format("https://restful-booker.herokuapp.com/booking");
        String body_request = "{\"firstname\":\"Filomena\",\"lastname\":\"Greenfelder\",\"totalprice\":510,\"depositpaid\":true,\"bookingdates\":{\"checkin\":\"2022-12-03\",\"checkout\":\"2019-01-01\"},\"additionalneeds\":\"Breakfast\"}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .headers("Content-Type","application/json")
                .headers("Accept", "*/*")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response" + body_response);

        //Junit 5 - Pruebas

        System.out.println("status response" + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //Validar que el body response no venga vacio
        assertNotNull(body_response);

        //Validar que el body response contenga algunos campos.
        assertTrue(body_response.contains("bookingid"));
        assertTrue(body_response.contains("firstname"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));
    }
}