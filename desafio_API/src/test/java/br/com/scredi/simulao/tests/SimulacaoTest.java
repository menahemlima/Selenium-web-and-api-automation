package br.com.scredi.simulao.tests;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class SimulacaoTest{

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/";
        RestAssured.port = 8080;
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        RestAssured.requestSpecification = reqBuilder.build();
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(5000L));
        RestAssured.responseSpecification = resBuilder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    /* Criar uma simulação */
    @Test
    public void simulacaoComSucessoTest() {

        given()
                .body(" {\n" +
                        "    \"nome\": \"Pessoa01\",\n" +
                        "    \"cpf\": \"11122233345\",\n" +
                        "    \"email\": \"pessoa1@email.com\",\n" +
                        "    \"valor\": 2000,\n" +
                        "    \"parcelas\": 2,\n" +
                        "    \"seguro\": true\n" +
                        "  }")
                .contentType(ContentType.JSON)
        .when()
                .post("api/v1/simulacoes")
        .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void simulacaoSemValorTest() {

        given()
                .body(" {\n" +
                        "    \"nome\": \"Pessoa2\",\n" +
                        "    \"cpf\": \"11122233389\",\n" +
                        "    \"email\": \"pessoa2@email.com\",\n" +
                        "    \"parcelas\": 4,\n" +
                        "    \"seguro\": true\n" +
                        "  }")
                .contentType(ContentType.JSON)
        .when()
                .post("api/v1/simulacoes")
        .then()
                .assertThat()
                .statusCode(400)
                .body("erros.valor", is("Valor não pode ser vazio"));
    }

    @Test
    public void simulacaoComCPFCadastradoTest() {
    /* Status Code FAIL - Retornando status code(400) deveria aparecer (409)
   Message FAIL - Retornando mensagem "CPF duplicado", deveria aparecer "CPF já existente"*/

        given()
                .body(" {\n" +
                        "    \"nome\": \"Pessoa3\",\n" +
                        "    \"cpf\": 11122233345,\n" +
                        "    \"email\": \"pessoa3@email.com\",\n" +
                        "    \"valor\": 1692,\n" +
                        "    \"parcelas\": 3,\n" +
                        "    \"seguro\": true\n" +
                        "  }")
                .contentType(ContentType.JSON)
        .when()
              .post("api/v1/simulacoes")
        .then()
                .log().all()
                .assertThat()
                .statusCode(409);
    }

    /* Alterar uma simulação */
    @Test
    public void simulacaoAlteraDadosTest() {

        String cpfCadastrado = "11122233345";

        given()
                .pathParam("cpf", cpfCadastrado)
                .body(" {\n" +
                        "    \"nome\": \"Pessoa3\",\n" +
                        "    \"cpf\": " + cpfCadastrado + ",\n" +
                        "    \"email\": \"pessoa3@email.com\",\n" +
                        "    \"valor\": 5000,\n" + // Dado alterado
                        "    \"parcelas\": 6,\n" + // Dado alterado
                        "    \"seguro\": true\n" +
                        "  }")
                .contentType(ContentType.JSON)
        .when()
                .put("api/v1/simulacoes/{cpf}")
        .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void simulacaoAlterarCPFNaoCadastradoTest() {

        String cpfNaoCadastrado = "11111111111";

        given()
                .pathParam("cpf", cpfNaoCadastrado)
                .body(" {\n" +
                        "    \"nome\": \"Pessoa4\",\n" +
                        "    \"cpf\": " + cpfNaoCadastrado + ",\n" +
                        "    \"email\": \"pessoa4@email.com\",\n" +
                        "    \"valor\": 789,\n" +
                        "    \"parcelas\": 4,\n" +
                        "    \"seguro\": true\n" +
                        "  }")
                .contentType(ContentType.JSON)
        .when()
                .put("api/v1/simulacoes/{cpf}")
        .then()
                .assertThat()
                .statusCode(404)
                .body("mensagem", is("CPF " + cpfNaoCadastrado + " não encontrado"));
    }

    /* Consulta todas as simulações cadastradas */
    @Test
    public void simulacaoRetornaTodosCadastrosTest() {

        given()
        .when()
               .get("api/v1/simulacoes")
        .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void simulacaoRetornaNenhumaSimulacaoTest() {
    /* Status Code FAIL - Retornando status code(204) deveria aparecer (200) */

        given()
        .when()
                .get("api/v1/simulacoes")
        .then()
                .assertThat()
                .statusCode(204);
    }

    /* Consulta uma simulação pelo CPF */
    @Test
    public void simulacaoConsultaCPFTest() {

        String cpfNaoCadastrado = "11122233345";

        given()
                .pathParam("cpf", cpfNaoCadastrado)
        .when()
                .get("api/v1/simulacoes/{cpf}")
        .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void simulacaoConsultaCPFNaoCadastradoTest() {

        String cpfCadastrado = "26276298085";

        given()
                .pathParam("cpf", cpfCadastrado)
        .when()
                .get("api/v1/simulacoes/{cpf}")
        .then()
                .assertThat()
                .statusCode(404);
    }

    /* Remover uma simulação */
    /* Status Code FAIL - Retornando status code(200) deveria aparecer (204) */
    @Test
    public void simulacaoRemoveUmaSimulacaoTest() {

        String idCadastrado = "13"; // id consultado previamente

        given()
                .pathParam("id", idCadastrado)
        .when()
                .delete("api/v1/simulacoes/{id}")
        .then()
                .assertThat()
                .statusCode(204);
    }

    /* Status Code FAIL - Retornando status code(200) deveria aparecer (404) */
    @Test
    public void simulacaoRemoveUmaSimulacaoNaoCadastradaTest() {

        String idNaoCadastrado = "789";

        given()
                .pathParam("id", idNaoCadastrado)
        .when()
                .delete("api/v1/simulacoes/{id}")
        .then()
                .assertThat()
                .statusCode(404);
    }
}
