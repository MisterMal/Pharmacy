package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.EXCEPTION_OPTIMISTIC_LOCK;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationExceptionOptimisticLock extends ApplicationException {

  ApplicationExceptionOptimisticLock() {
    super(CONFLICT, EXCEPTION_OPTIMISTIC_LOCK);
  }
}
