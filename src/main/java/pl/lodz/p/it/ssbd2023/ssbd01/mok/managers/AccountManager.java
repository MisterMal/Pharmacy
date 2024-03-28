package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AuthApplicationException.accountBlockedException;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AuthApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.security.HashAlgorithmImpl;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;
import pl.lodz.p.it.ssbd2023.ssbd01.util.email.EmailService;
import pl.lodz.p.it.ssbd2023.ssbd01.util.mergers.AccessLevelMerger;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@DenyAll
public class AccountManager extends AbstractManager implements AccountManagerLocal, SessionSynchronization {

  @Inject private AccountFacade accountFacade;

  @Inject private TokenManagerLocal verificationManager;

  @Inject
  @ConfigProperty(name = "unconfirmed.account.deletion.timeout.hours")
  private int UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS;

  @Inject
  @ConfigProperty(name = "unblocking.account.timeout.hours")
  private int UNBLOCKING_ACCOUNT_TIMEOUT_HOURS;

  @Inject
  @ConfigProperty(name = "max.incorrect.login.attempts")
  private int MAX_INCORRECT_LOGIN_ATTEMPTS;

  @Inject
  @ConfigProperty(name = "temporary.account.block.hours")
  private int TEMPORARY_ACCOUNT_BLOCK_HOURS;

  @Inject private EmailService emailService;
  @Context
  private SecurityContext context;

  @Override
  @RolesAllowed("getAllAccounts")
  public List<Account> getAllAccounts() {
    return accountFacade.findAll();
  }

  @Override
  @PermitAll
  public Account findByLogin(String login) {
    return accountFacade.findByLogin(login);
  }

  @Override
  @RolesAllowed("getCurrentUser")
  public Account getCurrentUser() {
    return accountFacade.findByLogin(getCurrentUserLogin());
  }

  @Override
  @PermitAll
  public String getCurrentUserLogin() {
    return context.getUserPrincipal().getName();
  }

  @Override
  @RolesAllowed("getCurrentUserWithAccessLevels")
  public Account getCurrentUserWithAccessLevels() {
    return accountFacade.findByLoginAndRefresh(getCurrentUserLogin());
  }

  @Override
  @RolesAllowed("getAccountAndAccessLevels")
  public Account getAccountAndAccessLevels(Long id) {
    Optional<Account> optionalAccount = accountFacade.findAndRefresh(id);
    if (optionalAccount.isEmpty()) {
      throw ApplicationException.createEntityNotFoundException();
    }
    return optionalAccount.get();
  }

  @Override
  @RolesAllowed("grantAccessLevel")
  public Account grantAccessLevel(Long id, AccessLevel accessLevel, String login, Long version) {
    Account account = getAccount(id);
    if(!account.getLogin().equals(login)) {
      throw ApplicationException.createMismatchedPayloadException();
    }
    if(!account.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    accessLevel.setAccount(account);
    accessLevel.setCreatedBy(getCurrentUserLogin());
    account.getAccessLevels().add(accessLevel);
    account.setModifiedBy(getCurrentUserLogin());
    account.setModificationDate(new Date()); // force version increment on account
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("getAccessLevel")
  public AccessLevel getAccessLevel(Long id, Role role) {
    Account account = getAccount(id);
    return AccessLevelFinder.findAccessLevel(account, role);
  }

  @Override
  @RolesAllowed("deactivateAccessLevel")
  public void deactivateAccessLevel(Long id, Role role) {
    Account account = getAccountAndAccessLevels(id);
    if(Objects.equals(account.getLogin(), getCurrentUserLogin()) && role == Role.ADMIN)
      throw AccountApplicationException.createDeactivationOfSelf();
    AccessLevel accessLevel = AccessLevelFinder.findAccessLevel(account, role);
    if(!accessLevel.getActive()) return;
    int numberActive = 0;
    for(AccessLevel l : account.getAccessLevels())
      if(l.getActive()) numberActive++;
    if(numberActive <= 1)
      throw AccountApplicationException.createDeactivateLastAccessLevel();
    accessLevel.setActive(false);
    account.setModifiedBy(getCurrentUserLogin());
    accessLevel.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
  }

  @Override
  @RolesAllowed("activateAccessLevel")
  public void activateAccessLevel(Long id, Role role) {
    Account account = getAccountAndAccessLevels(id);
    AccessLevel accessLevel = AccessLevelFinder.findAccessLevel(account, role);
    if(accessLevel.getActive()) return;
    accessLevel.setActive(true);
    account.setModifiedBy(getCurrentUserLogin());
    accessLevel.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
  }

  @Override
  @RolesAllowed("getAccount")
  public Account getAccount(Long id) {
    Optional<Account> optionalAccount = accountFacade.find(id);
    if (optionalAccount.isEmpty()) {
      throw ApplicationException.createEntityNotFoundException();
    }
    return optionalAccount.get();
  }

  @Override
  @PermitAll
  public void confirmAccountRegistration(String verificationToken) {
    verificationManager.verifyAccount(verificationToken);
  }

  @Override
  @PermitAll
  public Account registerAccount(Account account) {
    account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
    accountFacade.create(account);
    verificationManager.sendVerificationToken(account, null);
    return account;
  }

  @Override
  @RolesAllowed("createAccount")
  public Account createAccount(Account account) {
    account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
    account.setCreatedBy(getCurrentUserLogin());
    accountFacade.create(account);
    return account;
  }

  @Override
  @RolesAllowed("editAccessLevel")
  public Account editAccessLevel(Long id, AccessLevel accessLevel, Long version) {
    Account account = getAccount(id);
    AccessLevel found = AccessLevelFinder.findAccessLevel(account, accessLevel.getRole());
    if (!Objects.equals(found.getVersion(), version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    AccessLevelMerger.mergeAccessLevels(found, accessLevel);
    found.setModifiedBy(getCurrentUserLogin());
    account.setModificationDate(new Date());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("editSelfAccessLevel")
  public Account editSelfAccessLevel(AccessLevel accessLevel, Long version) {
    Account account = getCurrentUserWithAccessLevels();
    if (!Objects.equals(account.getVersion(), version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    AccessLevel found = AccessLevelFinder.findAccessLevel(account, accessLevel.getRole());
    AccessLevelMerger.mergeAccessLevels(found, accessLevel);
    found.setModifiedBy(getCurrentUserLogin());
    account.setModificationDate(new Date());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("activateUserAccount")
  public Account activateUserAccount(Long id) {
    Account account = getAccount(id);
    account.setConfirmed(true);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("blockAccount")
  public void blockAccount(Long id) {
    Account account = getAccount(id);
    if (!account.getActive()) {
      return;
    }
    account.setActive(false);
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    emailService.sendEmailAccountBlocked(
        account.getEmail(), account.getLogin(), account.getLanguage());
  }

  @Override
  @RolesAllowed("unblockAccount")
  public void unblockAccount(Long id) {
    Account account = getAccount(id);
    if (account.getActive()) {
      return;
    }
    account.setActive(true);
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    emailService.sendEmailAccountUnblocked(
        account.getEmail(), account.getLogin(), account.getLanguage());
  }

  @Override
  @PermitAll
  public void sendResetPasswordToken(String email) {
    Account account = accountFacade.findByEmail(email);
    verificationManager.sendResetPasswordToken(account);
  }

  @Override
  @PermitAll
  public void setNewPassword(String token, String newPassword) {
    String password = HashAlgorithmImpl.generate(newPassword);
    verificationManager.setNewPassword(token, password);
  }

  @Override
  @RolesAllowed("updateUserPassword")
  public Account updateUserPassword(Long id, String newPassword, String login, Long version) {
    Account account = getAccount(id);
    if(!account.getLogin().equals(login)) {
      throw ApplicationException.createMismatchedPayloadException();
    }
    if(!account.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    account.setPassword(HashAlgorithmImpl.generate(newPassword));
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("updateOwnPassword")
  public Account updateOwnPassword(String oldPassword, String newPassword, String login, Long version) {
    Account account = getCurrentUser();
    if(!account.getLogin().equals(login)) {
      throw ApplicationException.createMismatchedPayloadException();
    }
    if(!account.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    if (!HashAlgorithmImpl.check(oldPassword, account.getPassword())) {
      throw AuthApplicationException.createInvalidLoginOrPasswordException();
    }
    if (HashAlgorithmImpl.check(newPassword, account.getPassword())) {
      throw ApplicationException.createPasswordNotChangedException();
    }
    account.setPassword(HashAlgorithmImpl.generate(newPassword));
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("updateOwnEmail")
  public Account updateOwnEmail(String email, Long version) {
    Account account = getCurrentUser();
    if(!account.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    account.setConfirmed(false);
    account.setEmail(email);
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    verificationManager.sendEmailChangeEmail(account, email);
    return account;
  }

  @Override
  @RolesAllowed("changeAccountLanguage")
  public void changeAccountLanguage(String language) {
    try {
      LanguageType.valueOf(language);
    } catch(IllegalArgumentException e) {
      throw ApplicationException.createLanguageNotFoundException();
    }
    Locale locale = Locale.forLanguageTag(language);
    Account account = getCurrentUser();
    account.setLanguage(locale);
    accountFacade.edit(account);
  }

  @Override
  @RolesAllowed("updateUserEmail")
  public Account updateUserEmail(Long id, String email, String login, Long version) {
    Account account = getAccount(id);
    if(!account.getLogin().equals(login)) {
      throw ApplicationException.createMismatchedPayloadException();
    }
    if(!account.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    account.setEmail(email);
    account.setConfirmed(true);
    account.setModifiedBy(getCurrentUserLogin());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @PermitAll
  public void purgeUnactivatedAccounts() {
    List<Account> accountsToPurge = accountFacade.findNotConfirmed();

    for (Account account : accountsToPurge) {
      LocalDateTime timeoutThreshold =
          LocalDateTime.now().minusHours(UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS);
      LocalDateTime creationDate =
          Instant.ofEpochMilli(account.getCreationDate().getTime())
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      if (creationDate.isBefore(timeoutThreshold)) {
        accountFacade.remove(account);
        emailService.sendEmailWhenRemovedDueToNotConfirmed(account.getEmail(), account.getLogin(),
                account.getLanguage());
      }
    }
  }

  @Override
  @PermitAll
  public void activateBlockedAccounts() {
    List<Account> blockedAccounts = accountFacade.findNotActiveAndIncorrectLoginAttemptsEqual(
            MAX_INCORRECT_LOGIN_ATTEMPTS);

    for (Account account : blockedAccounts) {
      LocalDateTime timeoutThreshold = LocalDateTime.now()
              .minusHours(UNBLOCKING_ACCOUNT_TIMEOUT_HOURS);
      LocalDateTime lastNegativeLogin = Instant.ofEpochMilli(account.getLastNegativeLogin().getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
      if (lastNegativeLogin.isBefore(timeoutThreshold)) {
        account.setActive(true);
        accountFacade.edit(account);
        emailService.sendEmailAccountActivated(account.getEmail(), account.getLogin(), account.getLanguage());
      }
    }
  }

  @Override
  @PermitAll
  public void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect) {
    Account account = accountFacade.findByLogin(caller);
    if (account.getIncorrectLoginAttempts() >= MAX_INCORRECT_LOGIN_ATTEMPTS) {
      LocalDateTime timeoutThreshold =
          LocalDateTime.now().minusHours(TEMPORARY_ACCOUNT_BLOCK_HOURS);
      LocalDateTime lastIncorrectLogin =
          Instant.ofEpochMilli(account.getLastNegativeLogin().getTime())
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      if (lastIncorrectLogin.isBefore(timeoutThreshold)) {
        account.setActive(true);
      }
    }

    if (!account.getActive()) {
      throw accountBlockedException();
    }
    if (isCorrect) {
      account.setLastPositiveLogin(now);
      account.setLogicalAddress(remoteAddr);
      account.setIncorrectLoginAttempts(0);
    } else {
      account.setLastNegativeLogin(now);
      account.setLogicalAddress(remoteAddr);
      account.setIncorrectLoginAttempts(account.getIncorrectLoginAttempts() + 1);
      if (account.getIncorrectLoginAttempts() >= MAX_INCORRECT_LOGIN_ATTEMPTS) {
        account.setActive(false);
        emailService.sendEmailAccountBlockedTooManyLogins(account.getEmail(), account.getLogin(),
                account.getLanguage());
      }
    }
  }

  @Override
  @PermitAll
  public void sendVerificationTokenIfPreviousWasNotSent() {

    Date halfExpirationDate =
        Date.from(
            Instant.now().minus(UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS / 2, ChronoUnit.HOURS));

    List<Token> tokensToResend =
        verificationManager.findTokensByTokenTypeAndExpirationDateBefore(
            TokenType.VERIFICATION, halfExpirationDate);

    tokensToResend.forEach(
        token -> {
          token.setWasPreviousTokenSent(true);
          verificationManager.sendVerificationToken(token.getAccount(), token.getCode());
        });
  }

  @Override
  @RolesAllowed("confirmEmailChange")
  public void confirmEmailChange(String code) {
    verificationManager.confirmEmailChange(code);
  }
}
