package pl.lodz.p.it.ssbd2023.ssbd01.mok.scheduled;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;

@Startup
@Singleton
public class AccountScheduledTask {

  @Inject AccountManagerLocal accountManager;

  //    @Schedule(hour = "*/24", minute = "0", second = "0", info = "Each day")
  //    public void sendVerificationToken() {
  //        accountManager.getAllAccounts().forEach(accountManager::sendVerificationToken);
  //    }
  //
  //    public void purgeUnconfirmedAccounts() {
  //        accountManager.purgeUnconfirmedAccounts();
  //    }

  @Schedule(hour = "*", minute = "0", second = "20", info = "Each hour")
  @PermitAll
  public void activateBlockedAccounts() {
    accountManager.activateBlockedAccounts();
  }

  @Schedule(hour = "*", minute = "0", second = "10", info = "Each hour")
  @PermitAll
  public void purgeUnactivatedAccounts() {
    accountManager.purgeUnactivatedAccounts();
  }

  @Schedule(hour = "*", minute = "0", second = "0", info = "Each hour")
  @PermitAll
  public void sendVerificationTokenIfPreviousWasNotConfirmed() {
    accountManager.sendVerificationTokenIfPreviousWasNotSent();
  }
}
