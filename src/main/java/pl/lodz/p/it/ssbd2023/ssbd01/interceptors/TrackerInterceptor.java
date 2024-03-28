package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public class TrackerInterceptor {

  @Resource private SessionContext sessionContext;

  @AroundInvoke
  public Object intercept(InvocationContext context) throws Exception {
    StringBuilder message = new StringBuilder("Method: ");
    Object result;
    try {
      message
              .append(context.getMethod().getName())
              .append(" Class: ")
              .append(context.getTarget().getClass().getCanonicalName())
          .append(" user: ")
          .append(sessionContext.getCallerPrincipal().getName()).append(" ");
      log.fine(message.toString());

      result = context.proceed();

    } catch (Exception e) {
      message.append("finished with exception: ").append(e);
      log.log(Level.SEVERE, message.toString(), e);
      throw e;
    }

    message.append("finished successfully");

    log.info(message.toString());

    return result;
  }
}
