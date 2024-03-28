package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.MedicationCreateShipmentDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controller.dataForTests.chemistLoginDto;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Testcontainers
public class ShipmentControllerIT extends BaseTest {

    @AfterAll
    static void end() {
        afterAll();
    }
    static String chemistJwt;

    @BeforeAll
    static void setUp() {
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
    class CreateShipment {

        static CreateShipmentDTO createShipmentDTO;

        @BeforeAll
        static void setNewVersions() {
            List<CreateShipmentMedicationDTO> medications = new ArrayList<>();

            var responseMed1 = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/1")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String etag1 = responseMed1.getHeader("ETag").replace("\"", "");
            Long version1 = responseMed1.getBody().jsonPath().getLong("version");
            String price1 = responseMed1.getBody().jsonPath().getString("currentPrice");
            String name1 = responseMed1.getBody().jsonPath().getString("name");

            medications.add(CreateShipmentMedicationDTO.builder()
                    .quantity(2)
                    .medication(MedicationCreateShipmentDTO.builder()
                            .name(name1)
                            .version(version1)
                            .etag(etag1)
                            .price(BigDecimal.valueOf(Double.parseDouble(price1)))
                            .build())
                    .build());

            var responseMed2 = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/2")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String etag2 = responseMed2.getHeader("ETag").replace("\"", "");
            Long version2 = responseMed2.getBody().jsonPath().getLong("version");
            String price2 = responseMed2.getBody().jsonPath().getString("currentPrice");
            String name2 = responseMed2.getBody().jsonPath().getString("name");

            medications.add(CreateShipmentMedicationDTO.builder()
                    .quantity(5)
                    .medication(MedicationCreateShipmentDTO.builder()
                            .name(name2)
                            .version(version2)
                            .etag(etag2)
                            .price(BigDecimal.valueOf(Double.parseDouble(price2)))
                            .build())
                    .build());

            createShipmentDTO = CreateShipmentDTO.builder()
                    .shipmentDate(LocalDateTime.now().toString())
                    .shipmentMedications(medications)
                    .build();
        }

        @Test
        @Order(1)
        public void createShipment_correct_priceNotChanged() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(2)
        public void createShipment_correct_priceChanged() {
            // version should not have been incremented
            MedicationCreateShipmentDTO m = createShipmentDTO.getShipmentMedications().get(0).getMedication();
            m.setPrice(m.getPrice().add(BigDecimal.valueOf(3)));
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(3)
        public void createShipment_optimisticLock() {
            // version should change
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CONFLICT.getStatusCode())
                    .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
        }

        @Test
        @Order(4)
        public void createShipment_invalidDate() {
            setNewVersions();
            createShipmentDTO.setShipmentDate("not a date");
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", equalTo(EXCEPTION_INCORRECT_DATE_FORMAT));
        }

        @Test
        @Order(5)
        public void createShipment_noMedications() {
            createShipmentDTO.setShipmentMedications(new ArrayList<>());
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @Order(6)
        public void createShipment_quantityEqualZero() {
            setNewVersions();
            createShipmentDTO.getShipmentMedications().get(0).setQuantity(0);
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @Order(7)
        public void createShipment_priceEqualZero() {
            setNewVersions();
            MedicationCreateShipmentDTO m = createShipmentDTO.getShipmentMedications().get(0).getMedication();
            m.setPrice(BigDecimal.valueOf(0));
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @Order(8)
        public void createShipment_medicationEtagNotValid() {
            setNewVersions();
            createShipmentDTO.getShipmentMedications().get(0).getMedication().setVersion(99999L);
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", equalTo(EXCEPTION_ETAG_INVALID));
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadShipment {
        @Test
        @Order(1)
        public void getShipment_correct() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment/1")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void getShipment_notFound() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment/999")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
        }

        @Test
        @Order(3)
        public void getAllShipments_correct() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }
}
