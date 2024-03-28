package pl.lodz.p.it.ssbd2023.ssbd01.common;

import static jakarta.ejb.TransactionAttributeType.NOT_SUPPORTED;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TransactionAttribute;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractManager {

  protected static final Logger LOGGER = Logger.getGlobal();

  @Resource SessionContext sctx;

  private String transactionId;

  private boolean lastTransactionRollback;

  @TransactionAttribute(NOT_SUPPORTED)
  public boolean isLastTransactionRollback() {
    return lastTransactionRollback;
  }

  public void afterBegin() {
    transactionId =
        Long.toString(System.currentTimeMillis())
            + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
    LOGGER.log(
        Level.INFO,
        "Transaction TXid={0} begin at {1}, " + " identity: {2}",
        new Object[] {
          transactionId, this.getClass().getName(), sctx.getCallerPrincipal().getName()
        });
  }

  public void beforeCompletion() {
    LOGGER.log(
        Level.INFO,
        "Transaction TXid={0} before completion at" + " {1} identity: {2}",
        new Object[] {
          transactionId, this.getClass().getName(), sctx.getCallerPrincipal().getName()
        });
  }

  public void afterCompletion(boolean committed) {
    lastTransactionRollback = !committed;
    LOGGER.log(
        Level.INFO,
        "Transaction TXid={0} ended at {1}" + "by {3}, identity {2}",
        new Object[] {
          transactionId,
          this.getClass().getName(),
          sctx.getCallerPrincipal().getName(),
          committed ? "CONFIRMATION" : "CANCELLATION"
        });
  }
}
