package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class CategoryControllerIT extends BaseTest {

    @AfterAll
    static void end() {
        afterAll();
    }
    static String chemistJwt;

    @BeforeAll
    static void setUp() throws InterruptedException {
        chemistJwt = given()
                .contentType("application/json")
                .body(chemistLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().jsonPath().getString("jwtToken");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }


    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AddCategory {
        @Test
        @Order(1)
        public void addCategory_correct() {
            given()
                    .header("Authorization", "Bearer " + chemistJwt)
                    .body(categoryDto)
                    .post(getApiRoot() + "/category/add-category")
                    .then()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(2)
        public void addCategory_incorrectSameName() {
            given()
                    .header("Authorization", "Bearer " + chemistJwt)
                    .body(categoryDto)
                    .post(getApiRoot() + "/category/add-category")
                    .then()
                    .statusCode(Response.Status.CONFLICT.getStatusCode());
        }

        @Test
        @Order(3)
        public void readMedications_correct() {
            given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(4)
        public void readCategories_correct() {
            given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/category/")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EditCategory {
        private String etag;

        @BeforeEach
        public void init() {
            var response = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/category/4")
                    .then()
                    .log()
                    .all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract()
                    .response();
            etag = response.getHeader("ETag").replace("\"", "");
            Long version = response.getBody().jsonPath().getLong("version");
            editCategoryDTO.setVersion(version);
        }

        @Test
        @Order(1)
        public void editCategory_correct() {

            var response = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/category/4")
                    .then()
                    .log()
                    .all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract()
                    .response();
            etag = response.getHeader("ETag").replace("\"", "");
            Long version = response.getBody().jsonPath().getLong("version");
            editCategoryDTO.setVersion(version);

            given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .header("If-Match", etag)
                    .body(editCategoryDTO)
                    .put(getApiRoot() + "/category/4/edit-category")
                    .then()
                    .log()
                    .all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void editCategory_badVersion() {
            editCategoryDTO.setVersion(100L);
            given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .header("If-Match", etag)
                    .body(editCategoryDTO)
                    .put(getApiRoot() + "/category/1/edit-category")
                    .then()
                    .log()
                    .all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

    }
}