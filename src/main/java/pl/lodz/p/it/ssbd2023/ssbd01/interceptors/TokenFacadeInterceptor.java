package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

@Log
public class TokenFacadeInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext invocationContext) throws Exception {
    try {
      return invocationContext.proceed();
    } catch (OptimisticLockException e) {
      throw e;
    } catch(NoResultException e) {
      throw ApplicationException.createEntityNotFoundException();
    } catch (PersistenceException e) {
      log.warning(e.getMessage());
      throw ApplicationException.createGeneralException(e);
    }
  }
}
