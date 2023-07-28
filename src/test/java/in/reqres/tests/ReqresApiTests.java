package in.reqres.tests;

import in.reqres.models.CreateUserModel;
import in.reqres.models.CreateUserResponseModel;
import in.reqres.models.GetListResourceResponseModel;
import in.reqres.models.GetSingleUserResponseModel;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.CommonSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresApiTests extends TestBase {

    @Test
    void getSingleUserTest() {

        GetSingleUserResponseModel getSingleUserResponse = step("Make request", () ->
                given(userRequestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(getSingleUserResponseSpec200)
                .extract().as(GetSingleUserResponseModel.class));

        step("Check response", () -> {
                    assertEquals(2, getSingleUserResponse.getData().getId());
                    assertEquals("Janet", getSingleUserResponse.getData().getFirst_name());
                    assertEquals("Weaver", getSingleUserResponse.getData().getLast_name());
                }
        );
    }

    @Test
    void getListResourceTest() {

        GetListResourceResponseModel getListResourceResponse = step("Make request", () ->
                given(userRequestSpec)
                .when()
                .get("/unknown")
                .then()
                .spec(getListResourceResponseSpec200)
                .extract().as(GetListResourceResponseModel.class));

        step("Check response", () -> {
                    assertEquals(12, getListResourceResponse.getTotal());
                    assertEquals(6, getListResourceResponse.getData().size());
                    assertEquals("cerulean", getListResourceResponse.getData().get(0).getName());
                    assertEquals("fuchsia rose", getListResourceResponse.getData().get(1).getName());
                    assertEquals("true red", getListResourceResponse.getData().get(2).getName());
                    assertEquals("aqua sky", getListResourceResponse.getData().get(3).getName());
                    assertEquals("tigerlily", getListResourceResponse.getData().get(4).getName());
                    assertEquals("blue turquoise", getListResourceResponse.getData().get(5).getName());
                }
        );
    }

    @Test
    void successfulCreateUserTest() {
        CreateUserModel createData = new CreateUserModel();
        createData.setName("morpheus");
        createData.setJob("leader");

        CreateUserResponseModel createResponse = step("Make request", () ->
                given(userRequestSpec)
                        .body(createData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec201)
                        .extract().as(CreateUserResponseModel.class));

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
