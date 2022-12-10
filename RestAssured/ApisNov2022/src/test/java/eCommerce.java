import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class eCommerce {
    static private String url_base = "webapi.segundamano.mx";
    static private String email = "ventas_450@mailinator.com";
    static private String password = "12345";
    static private String account_id = "";
    static private String access_token = "";
    static private String ad_id = "";
    static private String uuid = "";

    static private String account_id_solo = "";
    static private String addressID1 = "";

    @Name("Obtener Token")
    private String obtenerToken() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", url_base);
        String body_request = "{\"account\":{\"email\":\"" + email + "\"}}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(email, password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();
        JsonPath jsonResponse = response.jsonPath();
        account_id = jsonResponse.get("account.account_id");
        System.out.println("Account_ID: " + account_id);
        uuid =  jsonResponse.get("account.uuid");
        access_token = jsonResponse.get("access_token");
        String split[] = account_id.split("/", 4);
        account_id_solo = split[3];
        return access_token;
    }

    @Test
    @Order(1)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Validar el filtrado de categorias.")
    public void obtenerCategoriasFiltrado_200() {
        RestAssured.baseURI = String.format("https://%s/urls/v1/public", url_base);
        String body_request = "{\n" +
                "    \"filters\": [\n" +
                "        {\n" +
                "            \"price\": \"-60000\",\n" +
                "            \"category\": \"2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"price\": \"60000-80000\",\n" +
                "            \"category\": \"2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"price\": \"80000-100000\",\n" +
                "            \"category\": \"2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"price\": \"100000-150000\",\n" +
                "            \"category\": \"2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"price\": \"150000-\",\n" +
                "            \"category\": \"2020\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("lang", "es")
                .headers("Accept", "application/json, text/plain, */*")
                .body(body_request)
                .post("/ad-listing");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Test case: Listado de anuncios.")
    public void listadoDeAnuncios_200() {
        RestAssured.baseURI = String.format("https://%s/highlights/v1/public", url_base);

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("prio", "1")
                .queryParam("cat", "1100")
                .queryParam("lim", "16")
                .headers("Accept", "application/json, text/plain, */*")
                .get("/highlights");

        String body_response= response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        assertTrue(body_response.contains("list_id"));
    }

    @Test
    @Order(3)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Obtener informacion del usuario.")
    public void obtenerInformacionUsuario_200() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/private/accounts", url_base);
        String body_request = "{\"account\":{\"email\":\"" + email + "\"}}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(email, password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("account_id"));
        assertTrue(body_response.contains("token"));
        assertTrue(body_response.contains("uuid"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));

        JsonPath jsonResponse = response.jsonPath();
        access_token = jsonResponse.get("access_token");
        System.out.println("Token: " + access_token);

        account_id = jsonResponse.get("account.account_id");
        System.out.println("Account_ID: " + account_id);

        uuid =  jsonResponse.get("account.uuid");
        System.out.println("uuid: " + uuid);
    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test case: Crear un usuario.")
    public void crearUsuario_401() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", url_base);
        String new_user = "agente_ventas_test" + (Math.floor(Math.random()*6789)) + "@mailinator.com";
        String new_password = "12345";
        String body_request = "{\"account\":{\"email\":\""+new_user+"\"}}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(new_user, new_password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(401, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        assertTrue(body_response.contains("ACCOUNT_VERIFICATION_REQUIRED"));
        assertTrue(body_response.contains("error"));
    }

    @Test
    @Order(5)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Editar un usuario.")
    public void editarDatosUsuario_200() {
        //Datos ramdom - Fake
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String ramdomBoolean = faker.random().nextBoolean().toString();

        String token = obtenerToken();
        String body_request = "{\n" +
                "    \"account\": {\n" +
                "        \"name\": \""+fullName+"\",\n" +
                "        \"phone\": \" 2345781823 \",\n" +
                "        \"professional\":"+ramdomBoolean+",\n" +
                "        \"phone_hidden\":"+ramdomBoolean+"\n" +
                "    }\n" +
                "}";

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s",url_base,account_id);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Accept","*/*")
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Content-type","application/json")
                .body(body_request)
                .patch();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("name"));
        assertTrue(body_response.contains("phone"));
        assertTrue(body_response.contains("phone_hidden"));
        assertTrue(body_response.contains("professional"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);
    }

    @Test
    @Order(6)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Test case: Editar un usuario No Existente.")
    public void editarDatosUsuarioNoExistente_403() {
        //Datos ramdom - Fake
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String ramdomBoolean = faker.random().nextBoolean().toString();

        String token = obtenerToken();
        String body_request = "{\n" +
                "    \"account\": {\n" +
                "        \"name\": \""+fullName+"\",\n" +
                "        \"phone\": \" 2345781823 \",\n" +
                "        \"professional\":"+ramdomBoolean+",\n" +
                "        \"phone_hidden\":"+ramdomBoolean+"\n" +
                "    }\n" +
                "}";

        RestAssured.baseURI = String.format("https://%s/nga/api/v1/private/accounts/23423",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Accept","*/*")
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Content-type","application/json")
                .body(body_request)
                .patch();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(403, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("FORBIDDEN"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);
    }

    @Test
    @Order(7)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Crear un anuncio.")
    public void crearAnuncio_200() {
        String token = obtenerToken();
        System.out.println("Token en la prueba crear anuncio " + token);

        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up",url_base,uuid);
        String body_request = "{\n" +
                "    \"images\": \"2500130844.jpg\",\n" +
                "    \"category\": \"4100\",\n" +
                "    \"subject\": \"Tasty compro estampillas para colección\",\n" +
                "    \"body\": \"Compra y venta e intercambio de estampillas para colección.\",\n" +
                "    \"is_new\": \"0\",\n" +
                "    \"region\": \"12\",\n" +
                "    \"municipality\": \"323\",\n" +
                "    \"area\": \"42205\",\n" +
                "    \"price\": \"559\",\n" +
                "    \"phone_hidden\": \"true\",\n" +
                "    \"show_phone\": \"false\",\n" +
                "    \"contact_phone\": \"408-859-6668\"\n" +
                "}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("x-source","PHOENIX_DESKTOP")
                .header("Accept","*/*")
                .header("Content-type","application/json")
                .auth().preemptive().basic(uuid, access_token)
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("ad_id"));
        assertTrue(body_response.contains("subject"));
        assertTrue(body_response.contains("category"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);

        JsonPath jsonResponse = response.jsonPath();
        ad_id = jsonResponse.get("data.ad.ad_id");
    }

    @Test
    @Order(8)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Ver anuncio después de la creación.")
    public void verAnuncioDespuesDeCreacion_200() {
        crearAnuncio_200();
        RestAssured.baseURI = String.format("https://%s/ad-stats/v1/public/accounts/%s/ads/%s", url_base,account_id_solo, ad_id);

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .headers("Accept", "*/*")
                .get();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        assertTrue(body_response.contains("list_id"));
        System.out.println(ad_id);
        assertTrue(body_response.contains(ad_id));;
    }

    @Test
    @Order(9)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Consultar si el anuncio fue publicado .")
    public void consultarSiAnuncioFuePublicado_200() {
        crearAnuncio_200();
        RestAssured.baseURI = String.format("https://%s/urls/v1/public/ad-view", url_base);
        String body_request = "{\n" +
                "    \"ids\": [\n" +
                "        \""+ad_id+"\"\n" +
                "    ]\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("lang","es")
                .headers("Accept", "application/json,text/plain,*/*")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        assertTrue(body_response.contains(ad_id));;
    }

    @Test
    @Order(10)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Editar un anuncio existente.")
    public void editarAnuncio_200() {
        crearAnuncio_200();
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up/%s", url_base,uuid, ad_id);
        Faker faker = new Faker();
        String randomPrice = faker.random().nextInt(1, 1000).toString();
        String body_request = "{\n" +
                "    \"images\": \"2500130844.jpg\",\n" +
                "    \"category\": \"4100\",\n" +
                "    \"subject\": \"Organizamos compra de estampillas para colección\",\n" +
                "    \"body\": \"Compra y venta e intercambio de estampillas para colección.\",\n" +
                "    \"is_new\": \"0\",\n" +
                "    \"region\": \"12\",\n" +
                "    \"municipality\": \"323\",\n" +
                "    \"area\": \"42205\",\n" +
                "    \"price\": \""+randomPrice+"\",\n" +
                "    \"phone_hidden\": \"true\",\n" +
                "    \"show_phone\": \"false\",\n" +
                "    \"contact_phone\": \"408-859-6668\"\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("x-source","PHOENIX_DESKTOP")
                .header("Accept","*/*")
                .header("Content-type","application/json")
                .auth().preemptive().basic(uuid, access_token)
                .body(body_request)
                .put();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);

        assertTrue(body_response.contains(ad_id));
        assertTrue(body_response.contains(randomPrice));
        assertTrue(body_response.contains("subject"));
    }

    @Test
    @Order(11)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Ver anuncio después de la edición.")
    public void verAnuncioDespuesDeEdicion_200() {
        editarAnuncio_200();
        RestAssured.baseURI = String.format("https://%s/ad-stats/v1/public/accounts/%s/ads/%s", url_base,account_id_solo, ad_id);

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .headers("Accept", "*/*")
                .get();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        assertTrue(body_response.contains("list_id"));
        System.out.println(ad_id);
        assertTrue(body_response.contains(ad_id));;
    }

    @Test
    @Order(12)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test case: Eliminar un anuncio sin autorización.")
    public void EliminarAnuncioSinAutorizacion_401() {
        crearAnuncio_200();
        String new_user = "agente_ventas_test" + (Math.floor(Math.random()*6789)) + "@mailinator.com";
        String new_password = "12345";
        String token = obtenerToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s/klfst/%s", url_base,account_id, ad_id);
        String body_request = "{\n" +
                "    \"delete_reason:\"{\n" +
                "        \"code\": \"2\"\n" +
                "    }\n" +
                "}";
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Accept","*/*")
                .header("Content-type","application/json")
                .auth().preemptive().basic(new_user, new_password)
                .body(body_request)
                .delete();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(401, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        assertTrue(body_response.contains("UNAUTHORIZED"));
    }

    @Test
    @Order(13)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Crear una dirección.")
    public void crearDireccion_201() {
        String token = obtenerToken();
        //Datos ramdom - Fake
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/create",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .formParam("contact",fullName)
                .formParam("phone","9878675654")
                .formParam("rfc","XAXX010101000")
                .formParam("zipCode","11011")
                .formParam("exteriorInfo","Morelia 4567")
                .formParam("interiorInfo","3")
                .formParam("region","11")
                .formParam("municipality","300")
                .formParam("area","8082")
                .formParam("alias","La casa test")
                .contentType("application/x-www-form-urlencoded")
                .header("Accept","*/*")
                .auth().preemptive().basic(uuid,token)
                .post();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(201, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("addressID"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);

        JsonPath jsonResponse = response.jsonPath();
        addressID1 = jsonResponse.get("addressID");
        System.out.println(addressID1);
    }

    @Test
    @Order(14)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: Editar una dirección existente.")
    public void editarDireccionExistente_200() {
        String token = obtenerToken();
        crearDireccion_201();
        //Datos ramdom - Fake
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String interiorInfo = faker.random().nextInt(1, 500).toString();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/modify/%s",url_base, addressID1);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .formParam("contact",fullName)
                .formParam("phone","9878675654")
                .formParam("rfc","XAXX010101000")
                .formParam("zipCode","11011")
                .formParam("exteriorInfo","Morelia 4567")
                .formParam("interiorInfo",interiorInfo)
                .formParam("region","11")
                .formParam("municipality","300")
                .formParam("area","8082")
                .formParam("alias","La casa test")
                .contentType("application/x-www-form-urlencoded")
                .header("Accept","*/*")
                .auth().preemptive().basic(uuid,token)
                .put();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains(addressID1));
        assertTrue(body_response.contains("modified correctly"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);
    }

    @Test
    @Order(15)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Test case: Editar una dirección no existente.")
    public void editarDireccionNoExistente_400() {
        String token = obtenerToken();
        //Datos ramdom - Fake
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String interiorInfo = faker.random().nextInt(1, 500).toString();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/modify/232",url_base);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .formParam("contact",fullName)
                .formParam("phone","9878675654")
                .formParam("rfc","XAXX010101000")
                .formParam("zipCode","11011")
                .formParam("exteriorInfo","Morelia 4567")
                .formParam("interiorInfo",interiorInfo)
                .formParam("region","11")
                .formParam("municipality","300")
                .formParam("area","8082")
                .formParam("alias","La casa test")
                .contentType("application/x-www-form-urlencoded")
                .header("Accept","*/*")
                .auth().preemptive().basic(uuid,token)
                .put();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(400, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains("Error during update"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);
    }

    @Test
    @Order(16)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test case:Eliminar una dirección.")
    public void eliminarDireccion_200() {
        String token = obtenerToken();
        crearDireccion_201();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/delete/%s",url_base, addressID1);
        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/x-www-form-urlencoded")
                .header("Accept","*/*")
                .auth().preemptive().basic(uuid,token)
                .delete();

        String body_response = response.getBody().prettyPrint();

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        assertTrue(body_response.contains(addressID1));
        assertTrue(body_response.contains("deleted correctly"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3500);
    }

    @Test
    @Order(17)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Test case: Obtener listado  de categorias.")
    public void obtenerListadoCategorias_200() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/public/categories/filter?lang=es", url_base);

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("lang", "es")
                .headers("Accept", "*/*")
                .get();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        System.out.println("status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        assertNotNull(body_response);

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1500);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));
    }
}