package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import java.sql.SQLException;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

@Log
public class GenericFacadeExceptionsInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext ictx) throws ApplicationException {
    try {
      return ictx.proceed();
    } catch (OptimisticLockException e) {
      throw ApplicationException.createOptimisticLockException();
    } catch (PersistenceException | SQLException e) {
      throw ApplicationException.createPersistenceException(e);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.warning(e.getMessage());
      throw ApplicationException.createGeneralException(e);
    }
  }
}
