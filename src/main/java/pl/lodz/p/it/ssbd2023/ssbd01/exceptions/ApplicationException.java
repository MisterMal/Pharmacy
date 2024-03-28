package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.*;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationException extends WebApplicationException {

  @Getter private Throwable cause;

  @Getter private final String key;

  protected ApplicationException(Response.Status status, String key, Throwable cause) {
    super(status);
    this.key = key;
    this.cause = cause;
  }

  protected ApplicationException(Response.Status status, String key) {
    super(status);
    this.key = key;
  }

  public static ApplicationException createGeneralException(Throwable cause) {
    return new ApplicationException(INTERNAL_SERVER_ERROR, EXCEPTION_GENERAL, cause);
  }

  public static ApplicationException createPersistenceException(Exception cause) {
    return new ApplicationException(INTERNAL_SERVER_ERROR, EXCEPTION_PERSISTENCE, cause);
  }

  public static ApplicationException createMismatchedPayloadException() {
    return new ApplicationException(BAD_REQUEST, EXCEPTION_MISMATCHED_PAYLOAD);
  }

  public static ApplicationException createAccessDeniedException() {
    return new ApplicationException(FORBIDDEN, EXCEPTION_ACCESS_DENIED);
  }

  public static ApplicationException createUnauthorisedException() {
    return new ApplicationException(UNAUTHORIZED, EXCEPTION_UNAUTHORISED);
  }

  public static ApplicationException createPasswordNotChangedException() {
    return new ApplicationException(BAD_REQUEST, EXCEPTION_PASSWORD_NOT_CHANGED);
  }

  public static ApplicationException createNotFoundException() {
    return new ApplicationException(NOT_FOUND, EXCEPTION_NOT_FOUND);
  }

  public static ApplicationException createMethodNotAllowedException() {
    return new ApplicationException(METHOD_NOT_ALLOWED, EXCEPTION_METHOD_NOT_ALLOWED);
  }

  public static ApplicationException createEtagEmptyException() {
    return new ApplicationException(BAD_REQUEST, EXCEPTION_ETAG_EMPTY);
  }

  public static ApplicationException createEtagNotValidException() {
    return new ApplicationException(BAD_REQUEST, EXCEPTION_ETAG_INVALID);
  }

  public static ApplicationException createEtagCreationException() {
    return new ApplicationException(UNAUTHORIZED, EXCEPTION_ETAG_CREATION);
  }

  public static ApplicationException createEntityNotFoundException() {
    return new ApplicationExceptionEntityNotFound();
  }

  public static ApplicationException createLanguageNotFoundException() {
    return new ApplicationException(NOT_FOUND, EXCEPTION_LANGUAGE_NOT_FOUND);
  }

  public static ApplicationException createOptimisticLockException() {
    return new ApplicationExceptionOptimisticLock();
  }

  public static ApplicationException createMedicationAlreadyExistsException() {
    return new ApplicationException(CONFLICT, EXCEPTION_MEDICATION_ALREADY_EXISTS);
  }

  public static ApplicationException createMedicationCategoryNotFoundException() {
    return new ApplicationException(NOT_FOUND, EXCEPTION_MEDICATION_CATEGORY_NOT_FOUND);
  }

  public static ApplicationException createIncorrectDateFormatException() {
    return new ApplicationException(BAD_REQUEST, EXCEPTION_INCORRECT_DATE_FORMAT);
  }

  public static ApplicationException createCategoryAlreadyExistsException() {
    return new ApplicationException(CONFLICT, EXCEPTION_CATEGORY_ALREADY_EXISTS);
  }
}
