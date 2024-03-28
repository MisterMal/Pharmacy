package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderPrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.MedicationCreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentMedicationDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Testcontainers
public class OrderControllerIT extends BaseTest {

    @AfterAll
    static void end() {
        afterAll();
    }
    static String patientJwt;
    static String chemistJwt;
    static String adminJwt;


    @BeforeAll
    static void setUp() throws InterruptedException {
        System.out.println(getApiRoot());
        patientJwt = given()
                .contentType("application/json")
                .body(patientLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");
        chemistJwt = given()
                .contentType("application/json")
                .body(chemistLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");
        adminJwt = given()
                .contentType("application/json")
                .body(adminLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");

        RestAssured.requestSpecification =
                new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .log(LogDetail.ALL)
                        .build();
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetOrdersToApprove {
        @Test
        @Order(1)
        public void createOrderMedication_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .get(getApiRoot() + "/order/to-approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApproveOrder {
        @Test
        @Order(1)
        public void approveOrder_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void approveOrder_incorrectStatus() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/approve")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION));
        }

        @Test
        @Order(3)
        public void approveOrder_notFound() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/999/approve")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ENTITY_NOT_FOUND));
        }
    }

    @Test
    @Order(1)
    public void cannot_read_self_orders_when_role_is_not_patient() {
        given()
                .header("Authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/order/self")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }



    @Test
    @Order(4)
    void only_chemist_should_update_queue() {
        given()
                .header("Authorization", "Bearer " + patientJwt)
                .put(getApiRoot() + "/order/update-queue")
                .then()
                .log().all()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CancelOrder {
        @Test
        @Order(1)
        public void cancelOrder_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/5/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void cancelOrder_incorrectStatus() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION));
        }

        @Test
        @Order(3)
        public void cancelOrder_notFound() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/999/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ENTITY_NOT_FOUND));
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteWaitingOrderById {
        @Test
        @Order(1)
        public void deleteWaitingOrderById_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .delete(getApiRoot() + "/order/1/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void deleteWaitingOrderById_not_in_queue() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .delete(getApiRoot() + "/order/1/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetOrder {
//        @Test
//        @Order(1)
//        public void getAllOrders_correct() {
//            given().header("Authorization", "Bearer " + chemistJwt)
//                    .log().all()
//                    .get(getApiRoot() + "/order/")
//                    .then().log().all()
//                    .statusCode(Response.Status.OK.getStatusCode());
//        }
        @Test
        @Order(2)
        public void getWaitingOrders_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .get(getApiRoot() + "/order/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WithdrawOrderById {
        @Test
        @Order(1)
        public void withdrawOrderById_bad_state() {
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/1/withdraw")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        }
        @Test
        @Order(2)
        public void withdrawOrderById_correct() {
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/6/withdraw")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(7)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateQueue {
        @Test
        @Order(2)
        void should_successfully_read_own_orders_as_patient() {
            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/order/self")
                    .then()
                    .log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(3)
        void should_change_orderStatus_when_update_queue() {
            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/order/self")
                    .then()
                    .log().all()
                    .body("[1].orderState", equalTo("IN_QUEUE"))
                    .statusCode(Response.Status.OK.getStatusCode());

            given()
                    .header("Authorization", "Bearer " + chemistJwt)
                    .put(getApiRoot() + "/order/update-queue")
                    .then()
                    .log().all()
                    .statusCode(Response.Status.NO_CONTENT.getStatusCode());

            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/order/self")
                    .then()
                    .log().all()
                    .body("[1].orderState", equalTo("FINALISED"))
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(8)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateOrder {

        private static CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .orderDate(LocalDateTime.now().toString())
                .orderMedications(new ArrayList<>())
                .build();

        private static int medicationOtcId = 7;
        private static int medicationOnPrescriptionId = 8;
        private static int medicationOnPrescription2Id = 9;
        private static CreateOrderMedicationDTO medicationOnPrescription;
        private static CreateOrderMedicationDTO medicationOnPrescription2;
        private static CreateOrderMedicationDTO medicationOtc;
        private static List<CreateShipmentMedicationDTO> shipmentMedications;

        private static CreateOrderMedicationDTO getCreateOrderMedication(int id) {
            var responseMed1 = given()
                    .header("authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/medication/" + id)
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String name = responseMed1.getBody().jsonPath().getString("name");

            return CreateOrderMedicationDTO.builder()
                    .quantity(5)
                    .name(name)
                    .build();
        }

        private static CreateShipmentMedicationDTO getCreateShipmentMedication(int id, int quantity) {
            var responseMed1 = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/" + id)
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String etag1 = responseMed1.getHeader("ETag").replace("\"", "");
            Long version1 = responseMed1.getBody().jsonPath().getLong("version");
            String price1 = responseMed1.getBody().jsonPath().getString("currentPrice");
            String name1 = responseMed1.getBody().jsonPath().getString("name");

            return CreateShipmentMedicationDTO.builder()
                    .quantity(quantity)
                    .medication(MedicationCreateShipmentDTO.builder()
                            .name(name1)
                            .version(version1)
                            .etag(etag1)
                            .price(BigDecimal.valueOf(Double.parseDouble(price1)))
                            .build())
                    .build();
        }

        private static void assertMediationStock(int id, int stock) {
            given().header("authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/medication/" + id)
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("stock", equalTo(stock));
        }

        @BeforeEach
        void setNewVersions() {
            medicationOtc = getCreateOrderMedication(medicationOtcId);
            medicationOnPrescription = getCreateOrderMedication(medicationOnPrescriptionId);
            medicationOnPrescription2 = getCreateOrderMedication(medicationOnPrescription2Id);

            createOrderDTO.setOrderMedications(new ArrayList<>());
            shipmentMedications = new ArrayList<>();
        }

        @Test
        @Order(1)
        public void submitOrder_otc_correct() {
            assertMediationStock(medicationOtcId, 15);
            createOrderDTO.getOrderMedications().add(medicationOtc);
            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
            assertMediationStock(medicationOtcId, 10);
        }

        @Test
        @Order(2)
        public void submitOrder_onPrescription_correct() {
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            createOrderDTO.setPrescription(new CreateOrderPrescriptionDTO("1235"));

            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
            assertMediationStock(medicationOnPrescriptionId, 10);
            assertMediationStock(medicationOtcId, 10);
        }

        @Test
        @Order(3)
        public void submitOrder_onPrescription_noPrescription() {
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            createOrderDTO.setPrescription(null);

            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", equalTo(EXCEPTION_PRESCRIPTION_REQUIRED));
        }

        @Test
        @Order(3)
        public void submitOrder_onPrescription_prescriptionExists() {
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            createOrderDTO.setPrescription(new CreateOrderPrescriptionDTO("1235"));

            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CONFLICT.getStatusCode())
                    .body("message", equalTo(EXCEPTION_PRESCRIPTION_ALREADY_EXISTS));
        }

        @Test
        @Order(4)
        public void submitOrder_notOnStock_single() {
            assertMediationStock(medicationOnPrescriptionId, 10);
            medicationOnPrescription.setQuantity(20);
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            createOrderDTO.setPrescription(new CreateOrderPrescriptionDTO("1236"));

            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
            assertMediationStock(medicationOnPrescriptionId, 10);
        }

        @Test
        @Order(5)
        public void submitOrder_notOnStock_multiple() {
            assertMediationStock(medicationOnPrescriptionId, 10);
            assertMediationStock(medicationOnPrescription2Id, 10);
            medicationOnPrescription.setQuantity(20); // not sufficient
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            assertMediationStock(medicationOnPrescription2Id, 10);
            medicationOnPrescription2.setQuantity(10); // sufficient
            createOrderDTO.getOrderMedications().add(medicationOnPrescription2);
            createOrderDTO.setPrescription(new CreateOrderPrescriptionDTO("1237"));

            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
            assertMediationStock(medicationOnPrescriptionId, 10);
            assertMediationStock(medicationOnPrescription2Id, 10);
        }

        @Test
        @Order(6)
        public void submitOrder_partiallyCalculateQueue_correct() {
            // assert beginning stock
            assertMediationStock(medicationOtcId, 10);
            assertMediationStock(medicationOnPrescriptionId, 10);
            assertMediationStock(medicationOnPrescription2Id, 10);

            // create shipment
            shipmentMedications.add(
                    getCreateShipmentMedication(medicationOnPrescriptionId, 30));
            shipmentMedications.add(
                    getCreateShipmentMedication(medicationOtcId, 10));
            CreateShipmentDTO createShipmentDTO = CreateShipmentDTO.builder()
                    .shipmentDate(LocalDateTime.now().toString())
                    .shipmentMedications(shipmentMedications)
                    .build();
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());

            // assert stock has not changed
            assertMediationStock(medicationOtcId, 10);
            assertMediationStock(medicationOnPrescriptionId, 10);
            assertMediationStock(medicationOnPrescription2Id, 10);

            // check status of orders
            var response = given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/order/self")
                    .then()
                    .log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            Assertions.assertEquals("IN_QUEUE", response.getBody().jsonPath().getString("[9].orderState"));
            Assertions.assertEquals("IN_QUEUE", response.getBody().jsonPath().getString("[10].orderState"));

            // create new order
            medicationOnPrescription.setQuantity(5);
            createOrderDTO.getOrderMedications().add(medicationOnPrescription);
            createOrderDTO.setPrescription(new CreateOrderPrescriptionDTO("1238"));
            given().header("Authorization", "Bearer " + patientJwt)
                    .body(createOrderDTO)
                    .log().all()
                    .post(getApiRoot() + "/order/submit")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());

            // assert stock has been computed
            assertMediationStock(medicationOtcId, 10); // not changed despite new shipment
            assertMediationStock(medicationOnPrescriptionId, 0); // previous orders in queue has been finalised
            assertMediationStock(medicationOnPrescription2Id, 0); // all medications in computed orders have been computed

            // check status of orders
            response = given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .get(getApiRoot() + "/order/self")
                    .then()
                    .log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            Assertions.assertEquals("WAITING_FOR_CHEMIST_APPROVAL", response.getBody().jsonPath().getString("[9].orderState"));
            Assertions.assertEquals("WAITING_FOR_CHEMIST_APPROVAL", response.getBody().jsonPath().getString("[10].orderState"));
            Assertions.assertEquals("IN_QUEUE", response.getBody().jsonPath().getString("[11].orderState"));
        }
    }

    @Nested
    @Order(9)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class patientApprovedById {
        @Test
        @Order(1)
        public void patientApprovedById_bad_State(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/2/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_NO_PERMISSION_TO_APPROVE_ORDER));
        }

        @Test
        @Order(2)
        public void patientApprovedById_not_found(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/500/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_NOT_FOUND));
        }

        @Test
        @Order(3)
        public void patientApprovedById_correct(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/7/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }
}
