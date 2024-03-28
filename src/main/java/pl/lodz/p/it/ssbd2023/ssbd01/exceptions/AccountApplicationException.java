package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.*;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

import jakarta.ws.rs.core.Response;

@jakarta.ejb.ApplicationException(rollback = true)
public class AccountApplicationException extends ApplicationException {

  private AccountApplicationException(Response.Status status, String key) {
    super(status, key);
  }

  private AccountApplicationException(Response.Status status, String key, Exception e) {
    super(status, key, e);
  }

  public static AccountApplicationException createAccountConstraintViolationException(Exception e) {
    return new AccountApplicationException(
        EXPECTATION_FAILED, EXCEPTION_ACCOUNT_CONSTRAINT_VIOLATION, e);
  }

  public static AccountApplicationException createAccountNotConfirmedException() {
    return new AccountApplicationException(
            UNAUTHORIZED, EXCEPTION_ACCOUNT_NOT_CONFIRMED);
  }

  public static AccountApplicationException createUndefinedAccessLevelException() {
    return new AccountApplicationException(BAD_REQUEST, EXCEPTION_ACCOUNT_NO_SUCH_ACCESS_LEVEL);
  }

  public static AccountApplicationException createDuplicateAccessLevelException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_ACCESS_LEVEL);
  }

  public static AccountApplicationException createDeactivationOfSelf() {
    return new AccountApplicationException(
            BAD_REQUEST, EXCEPTION_ACCOUNT_DEACTIVATE_SELF);
  }

  public static AccountApplicationException createDeactivateLastAccessLevel() {
    return new AccountApplicationException(
            BAD_REQUEST, EXCEPTION_ACCOUNT_DEACTIVATE_LAST_ACCESS_LEVEL);
  }

  public static AccountApplicationException createDuplicateEmailException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_EMAIL);
  }

  public static AccountApplicationException createDuplicateLoginException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_LOGIN);
  }

  public static AccountApplicationException createDuplicatePhoneNumberException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_PHONE_NUMBER);
  }

  public static AccountApplicationException createDuplicateNipException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_NIP);
  }

  public static AccountApplicationException createDuplicatePeselException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_PESEL);
  }
}
