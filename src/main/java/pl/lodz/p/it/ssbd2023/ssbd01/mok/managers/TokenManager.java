package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.incorrectTokenTypeException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenAlreadyUsedException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenExpiredException;

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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.util.email.EmailService;

@Stateful
@Log
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({
        GenericManagerExceptionsInterceptor.class,
        TrackerInterceptor.class
})
@DenyAll
public class TokenManager extends AbstractManager
    implements TokenManagerLocal, SessionSynchronization {

  @Inject TokenFacade tokenFacade;

  @Inject EmailService emailService;

  @Inject
  @ConfigProperty(name = "verification.token.expiration.hours")
  private int VERIFICATION_TOKEN_EXPIRATION_HOURS;

  @Inject
  @ConfigProperty(name = "reset-password.token.expiration.hours")
  private int RESET_PASSWORD_TOKEN_EXPIRATION_HOURS;

  @Override
  @PermitAll
  public void sendVerificationToken(Account account, String code) {
    Token token = makeToken(account, code, TokenType.VERIFICATION);
    tokenFacade.create(token);
    emailService.sendRegistrationEmail(
        account.getEmail(), account.getLogin(), account.getLanguage(), token.getCode());
  }

  @PermitAll
  private Token makeToken(Account account, String code, TokenType tokenType) {
    Token token =
        Token.builder()
            .account(account)
            .code(code == null ? RandomStringUtils.randomAlphanumeric(8) : code)
            .tokenType(tokenType)
            .wasPreviousTokenSent(false)
            .expirationDate(createExpirationDate(tokenType))
            .build();

    token.setCreatedBy(account.getLogin());

//    tokenFacade.create(token);
//    emailService.sendRegistrationEmail(account.getEmail(), account.getLogin(),
//            account.getLanguage(), token.getCode());
    return token;
  }

  @PermitAll
  private Date createExpirationDate(TokenType type) {
    long hours = (type == TokenType.VERIFICATION) ? VERIFICATION_TOKEN_EXPIRATION_HOURS :
            RESET_PASSWORD_TOKEN_EXPIRATION_HOURS;
    return new Date(
        Instant.now()
            .plusSeconds(hours * 60 * 60)
            .toEpochMilli());
  }

  @Override
  @PermitAll
  public void verifyAccount(String code) {
    Token token = tokenFacade.findByCode(code);
    checkIfTokenIsValid(token, TokenType.VERIFICATION);
    Account account = token.getAccount();
    account.setConfirmed(true);
    account.setModifiedBy(account.getLogin());
    token.setUsed(true);
    token.setModifiedBy(account.getLogin());
    tokenFacade.edit(token);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
  }

  @Override
  @RolesAllowed("updateOwnEmail")
  public void sendEmailChangeEmail(Account account, String new_email) {
    Token token = makeToken(account, encodeEmail(new_email), TokenType.VERIFICATION);
    tokenFacade.create(token);
    emailService.sendEmailChangeEmail(new_email, account.getLogin(), account.getLanguage(), token.getCode());
  }

  @RolesAllowed("updateOwnEmail")
  private String encodeEmail(String new_email) {
    return RandomStringUtils.randomAlphanumeric(8)
        + Base64.getEncoder().encodeToString(new_email.getBytes());
  }

  @RolesAllowed("confirmEmailChange")
  private String decodeEmail(String code) {
    return Base64.getDecoder().decode((code.substring(8, code.length())).getBytes()).toString();
  }

  @Override
  @RolesAllowed("confirmEmailChange")
  public void confirmEmailChange(String code) {
    Token token = tokenFacade.findByCode(code);
    checkIfTokenIsValid(token, TokenType.VERIFICATION);
    Account account = token.getAccount();
    account.setEmail(decodeEmail(code));
    account.setModifiedBy(account.getLogin());
    token.setUsed(true);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
    tokenFacade.edit(token);
  }

  @Override
  @PermitAll
  public List<Token> findTokensByTokenTypeAndExpirationDateBefore(
      TokenType verification, Date halfExpirationDate) {
    return tokenFacade.findByTypeAndBeforeGivenData(verification, halfExpirationDate);
  }

  @Override
  @PermitAll
  public void sendResetPasswordToken(Account account) {
    Token token = makeToken(account, null, TokenType.PASSWORD_RESET);
    tokenFacade.create(token);
    emailService.sendEmailResetPassword(
        account.getEmail(), account.getLogin(), account.getLanguage(), token.getCode());
  }

  @Override
  @PermitAll
  public void setNewPassword(String token, String newPassword) {
    Token foundToken = tokenFacade.findByCode(token);
    checkIfTokenIsValid(foundToken, TokenType.PASSWORD_RESET);

    Account account = foundToken.getAccount();
    account.setPassword(newPassword);
    account.setModifiedBy(account.getLogin());
    foundToken.setUsed(true);
    foundToken.setModifiedBy(account.getLogin());

    tokenFacade.edit(foundToken);
  }

  @PermitAll
  private void checkIfTokenIsValid(Token token, TokenType type) {
    if (token == null) {
      throw TokenException.tokenNotFoundException();
    }
    Account account = token.getAccount();

    if (token.getExpirationDate().before(new java.util.Date())) {
      throw tokenExpiredException();
    }
    if (token.isUsed()) {
      throw tokenAlreadyUsedException();
    }
    if (type == TokenType.VERIFICATION
        && account.getConfirmed()) {
      throw tokenAlreadyUsedException();
    }
    if (token.getTokenType() != type) {
      throw incorrectTokenTypeException();
    }
  }
}
