package br.com.scredi.simulao.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import io.restassured.RestAssured;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.*;

public class RestricaoTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/";
        RestAssured.port = 8080;
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        RestAssured.requestSpecification = reqBuilder.build();
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(5000L));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void verificarCPFSemRestricaoTest() throws Exception {

            String CPFInformado = "77788866600";

            given()
                    .pathParam("cpf", CPFInformado)
                    .when()
                    .get("api/v1/restricoes/{cpf}")
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(204);
        }

            @Test
            public void verificarCPFComRestricaoTest () throws Exception {

            String CPFInformado = "01317496094";

            given()
                    .pathParam("cpf", CPFInformado)
                    .when()
                    .get("api/v1/restricoes/{cpf}")
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("mensagem", is("O CPF " + CPFInformado + " tem problema"));
        }
}
