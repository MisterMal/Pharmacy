package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.*;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@jakarta.ejb.ApplicationException(rollback = true)
public class OrderException extends ApplicationException {

  private OrderException(Response.Status status, String key) {
    super(status, key);
  }

  private OrderException(Response.Status status, String key, Exception e) {
    super(status, key, e);
  }

  public static OrderException onlyPatientCanListOrders() {
    return new OrderException(FORBIDDEN, EXCEPTION_ORDER_ONLY_PATIENT_CAN_LIST_SELF_ORDERS);
  }

  public static OrderException orderNotInQueue() {
    return new OrderException(FORBIDDEN, EXCEPTION_ORDER_NOT_IN_QUEUE);
  }

  public static OrderException noPermissionToDeleteOrder() {
    return new OrderException(FORBIDDEN, EXCEPTION_NO_PERMISSION_TO_DELETE_ORDER);
  }

  public static OrderException createModificationOrderOfIllegalState() {
    return new OrderException(BAD_REQUEST, EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION);
  }

  public static OrderException noPermissionToApproveOrder() {
    return new OrderException(FORBIDDEN, EXCEPTION_NO_PERMISSION_TO_APPROVE_ORDER);
  }
  public static OrderException createPrescriptionRequired() {
    return new OrderException(BAD_REQUEST, EXCEPTION_PRESCRIPTION_REQUIRED);
  }
  public static OrderException createPrescriptionAlreadyExists() {
    return new OrderException(CONFLICT, EXCEPTION_PRESCRIPTION_ALREADY_EXISTS);
  }
  public static OrderException orderNotFound(Long id) {
    return new OrderException(FORBIDDEN, EXCEPTION_ORDER_NOT_FOUND);
  }


}
