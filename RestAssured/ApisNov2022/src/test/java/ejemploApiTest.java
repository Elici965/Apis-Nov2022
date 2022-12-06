import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class ejemplo_Api_Test {

    @Test
    public void api_test() {
        RestAssured.baseURI = String.format("https://reqres.in/api/users?page=2");
        Response response = given()
                .log.all
                .headers("Accept","*/*")
                .get();

        String boy_response = response.getBody().prettyPrint();
        System.out.println("Response" + body.response);
    }
}
