package in.reqres.tests;

import in.reqres.models.CreateBodyModel;
import in.reqres.models.CreateResponseModel;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.CommonSpec.createUserRequestSpec;
import static in.reqres.specs.CommonSpec.createUserResponseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTests extends TestBase {

    @Test
    void getSingleUserTest() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void getListResourceTest() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .when()
                .get("/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", hasSize(6))
                .body("total", is(12))
                .body("data.name", hasItems("cerulean", "fuchsia rose", "true red", "aqua sky", "tigerlily",
                        "blue turquoise"));
    }

    @Test
    void successfulCreateUserTest() {
        CreateBodyModel createData = new CreateBodyModel();
        createData.setName("morpheus");
        createData.setJob("leader");

        CreateResponseModel createResponse = step("Make request", () ->
                given(createUserRequestSpec)
                        .body(createData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateResponseModel.class));

        step("Check response", () -> {
                    assertEquals("morpheus", createResponse.getName());
                    assertEquals("leader", createResponse.getJob());
                }
        );
    }

    @Test
    void successfulUpdateUserTest() {

        String updateData = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(updateData)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void successfulDeleteUserTest() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
