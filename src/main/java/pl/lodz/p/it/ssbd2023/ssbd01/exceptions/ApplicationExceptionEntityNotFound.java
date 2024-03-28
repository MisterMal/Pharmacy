package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.EXCEPTION_ENTITY_NOT_FOUND;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationExceptionEntityNotFound extends ApplicationException {

  ApplicationExceptionEntityNotFound() {
    super(NOT_FOUND, EXCEPTION_ENTITY_NOT_FOUND);
  }
}
