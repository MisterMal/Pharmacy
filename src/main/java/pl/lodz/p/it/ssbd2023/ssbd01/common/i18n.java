package pl.lodz.p.it.ssbd2023.ssbd01.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class i18n {
  public static final String EXCEPTION_UNKNOWN = "exception.unknown";
  public static final String EXCEPTION_GENERAL = "exception.general";
  public static final String EXCEPTION_PERSISTENCE = "exception.persistence";
  public static final String EXCEPTION_ACCESS_DENIED = "exception.access-denied";
  public static final String EXCEPTION_UNAUTHORISED = "exception.unauthorised";
  public static final String EXCEPTION_PASSWORD_NOT_CHANGED = "exception.password.not-changed";
  public static final String EXCEPTION_NOT_FOUND = "exception.not-found";
  public static final String EXCEPTION_LANGUAGE_NOT_FOUND = "exception.language.not-found";
  public static final String EXCEPTION_METHOD_NOT_ALLOWED = "exception.method-not-allowed";
  public static final String EXCEPTION_MISMATCHED_PAYLOAD = "exception.mismatched.payload";
  public static final String EXCEPTION_ETAG_CREATION = "exception.etag.creation";
  public static final String EXCEPTION_ETAG_EMPTY = "exception.etag.empty";
  public static final String EXCEPTION_ETAG_INVALID = "exception.etag.not-valid";
  public static final String EXCEPTION_OPTIMISTIC_LOCK = "exception.optimistic-lock";
  public static final String EXCEPTION_ENTITY_NOT_FOUND = "exception.entity-not-found";
  public static final String EXCEPTION_INCORRECT_DATE_FORMAT =
          "exception.incorrect.date-format";
  public static final String EXCEPTION_MEDICATION_ALREADY_EXISTS =
      "exception.medication.already-exists";
  public static final String EXCEPTION_MEDICATION_CATEGORY_NOT_FOUND =
      "exception.medication.category.not-found";


  public static final String EXCEPTION_ACCOUNT_CONSTRAINT_VIOLATION =
      "exception.account.constraint-violation";
  public static final String EXCEPTION_ACCOUNT_NOT_CONFIRMED =
          "exception.account.not-confirmed";
  public static final String EXCEPTION_ACCOUNT_NO_SUCH_ACCESS_LEVEL =
      "exception.account.no-such-access-level";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_ACCESS_LEVEL =
      "exception.account.duplicate.access-level";
  public static final String EXCEPTION_ACCOUNT_DEACTIVATE_LAST_ACCESS_LEVEL =
      "exception.account.deactivate.last-access-level";
  public static final String EXCEPTION_ACCOUNT_DEACTIVATE_SELF =
      "exception.account.deactivate.self";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_EMAIL =
          "exception.account.duplicate.email";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_LOGIN =
          "exception.account.duplicate.login";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_PHONE_NUMBER =
          "exception.account.duplicate.phone";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_NIP =
          "exception.account.duplicate.nip";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_PESEL =
          "exception.account.duplicate.pesel";
  public static final String EXCEPTION_AUTH_BAD_CREDENTIALS = "exception.auth.bad-credentials";
  public static final String EXCEPTION_AUTH_BLOCKED_ACCOUNT = "exception.auth.blocked-account";

  public static final String EXCEPTION_TOKEN_EXPIRED = "exception.token.expired";
  public static final String EXCEPTION_TOKEN_NOT_FOUND = "exception.token.not-found";
  public static final String EXCEPTION_TOKEN_ALREADY_USED = "exception.token.already-used";
  public static final String EXCEPTION_TOKEN_BAD_TYPE = "exception.token.bad-type";

  public static final String MAIL_ACCOUNT_REMOVED_SUBJECT = "mail.account.removed.subject";
  public static final String MAIL_ACCOUNT_REMOVED_BODY = "mail.account.removed.body";
  public static final String MAIL_ACCOUNT_BLOCKED_SUBJECT = "mail.account.blocked.subject";
  public static final String MAIL_ACCOUNT_BLOCKED_BODY = "mail.account.blocked.body";
  public static final String MAIL_ACCOUNT_UNBLOCKED_SUBJECT = "mail.account.unblocked.subject";
  public static final String MAIL_ACCOUNT_UNBLOCKED_BODY = "mail.account.unblocked.body";
  public static final String MAIL_ACCOUNT_BLOCKED_TOO_MANY_LOGINS_BODY =
          "mail.account.unblocked.too-many-logins.body";
  public static final String MAIL_ACCOUNT_ACTIVATED_SUBJECT = "mail.account.activated.subject";
  public static final String MAIL_ACCOUNT_ACTIVATED_BODY = "mail.account.activated.body";
  public static final String MAIL_ACCOUNT_REGISTER_SUBJECT = "mail.account.register.subject";
  public static final String MAIL_ACCOUNT_REGISTER_BODY = "mail.account.register.body";

  public static final String MAIL_PASSWORD_RESET_SUBJECT = "mail.password.reset.subject";
  public static final String MAIL_PASSWORD_RESET_BODY = "mail.password.reset.body";
  public static final String MAIL_CHANGE_EMAIL_BODY = "mail.email.change.body";
  public static final String MAIL_CHANGE_EMAIL_SUBJECT = "mail.email.change.subject";

  public static final String EXCEPTION_ORDER_ONLY_PATIENT_CAN_LIST_SELF_ORDERS =
      "exception.order.only.patient.can.list.self.orders";
  public static final String EXCEPTION_ORDER_ONLY_CHEMIST_CAN_LIST_WAITING_ORDERS =
          "exception.order.only.chemist.can.list.waiting.orders";

  public static final String EXCEPTION_ORDER_ONLY_PATIENT_CAN_ORDER =
          "exception.order.only.patient.can.order";

  public static final String EXCEPTION_ORDER_NOT_IN_QUEUE =
          "exception.order_not_in_queue";
  public static final String EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION =
          "exception.order.illegal-state.modification";


  public static final String EXCEPTION_CATEGORY_ALREADY_EXISTS = "exception.category.already-exists";


  public static final String EXCEPTION_NO_PERMISSION_TO_DELETE_ORDER =
          "exception.no.permission.to.delete.order";

  public static final String EXCEPTION_NO_PERMISSION_TO_APPROVE_ORDER =
          "exception.no.permission.to.approve.order";
  public static final String EXCEPTION_PRESCRIPTION_REQUIRED =
          "exception.order.prescription.required";
  public static final String EXCEPTION_PRESCRIPTION_ALREADY_EXISTS =
          "exception.order.prescription.already-exists";

  public static final String EXCEPTION_ORDER_NOT_FOUND =
          "exception.order_not_found";

  public static String getMessage(String message, Locale locale, Object... parameters) {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale);
    String fmtString = bundle.getString(message);
    return String.format(locale, fmtString, parameters);
  }
}
