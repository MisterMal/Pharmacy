package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@Local
public interface AccountManagerLocal extends CommonManagerLocalInterface {

  Account createAccount(Account account);

  List<Account> getAllAccounts();

  Account findByLogin(String login);

  void confirmAccountRegistration(String token);

  Account getAccount(Long id);
  Account getCurrentUser();

  String getCurrentUserLogin();

  Account getCurrentUserWithAccessLevels();

  Account getAccountAndAccessLevels(Long id);

  Account registerAccount(Account account);

  Account editAccessLevel(Long id, AccessLevel accessLevel, Long version);
  Account editSelfAccessLevel(AccessLevel accessLevel, Long version);

  void confirmEmailChange(String token);

  Account grantAccessLevel(Long id, AccessLevel accessLevel, String login, Long version);
  AccessLevel getAccessLevel(Long id, Role role);

  void deactivateAccessLevel(Long id, Role role);

  void activateAccessLevel(Long id, Role role);

  Account activateUserAccount(Long id);

  void blockAccount(Long id);

  void unblockAccount(Long id);

  void sendResetPasswordToken(String email);

  void setNewPassword(String token, String newPassword);

  Account updateUserPassword(Long id, String newPassword, String login, Long version);

  Account updateOwnPassword(String oldPassword, String newPassword, String login, Long version);

  Account updateOwnEmail(String email, Long version);

  void changeAccountLanguage(String language);

  Account updateUserEmail(Long id, String email, String login, Long version);

  void purgeUnactivatedAccounts();

  void activateBlockedAccounts();

  void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect);

  void sendVerificationTokenIfPreviousWasNotSent();
}
