package in.reqres.tests;

import in.reqres.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.CommonSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresApiTests extends TestBase {

    @Test
    @DisplayName("Check getting single user data")
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
    @DisplayName("Check getting list resources")
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
    @DisplayName("Check successful user creation")
    void successfulCreateUserTest() {
        CreateUserModel createData = new CreateUserModel();
        createData.setName("morpheus");
        createData.setJob("leader");

        CreateUserResponseModel createUserResponse = step("Make request", () ->
                given(userRequestSpec)
                        .body(createData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec201)
                        .extract().as(CreateUserResponseModel.class));

        step("Check response", () -> {
                    assertEquals("morpheus", createUserResponse.getName());
                    assertEquals("leader", createUserResponse.getJob());
                }
        );
    }

    @Test
    @DisplayName("Check successful user updating")
    void successfulUpdateUserTest() {
        CreateUserModel updateData = new CreateUserModel();
        updateData.setName("morpheus");
        updateData.setJob("zion resident");

        UpdateUserResponseModel updateUserResponse = step("Make request", () ->
                given(userRequestSpec)
                        .body(updateData)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(updateUserResponseSpec200)
                        .extract().as(UpdateUserResponseModel.class));

        step("Check response", () -> {
                    assertEquals("morpheus", updateUserResponse.getName());
                    assertEquals("zion resident", updateUserResponse.getJob());
                }
        );
    }

    @Test
    @DisplayName("Check successful user deletion")
    void successfulDeleteUserTest() {

        step("Delete user", () ->
                given(userRequestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(deleteUserResponseSpec204));
    }
}
