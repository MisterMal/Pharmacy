package pl.lodz.p.it.ssbd2023.ssbd01.security;

import com.mailjet.client.errors.MailjetException;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.TokenDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationExceptionEntityNotFound;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AuthApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;

@Log
@Path("/auth")
@DenyAll
public class AuthController extends AbstractController {

  @Inject private IdentityStoreHandler identityStoreHandler;

  @Inject private JwtUtils jwtUtils;
  @Context private HttpServletRequest httpServletRequest;
  @Inject private AccountManagerLocal accountManager;

  @POST
  @Path("/login")
  @PermitAll
  public Response authenticate(@Valid LoginDTO loginDto) {
    Account account;
    try {
      account = repeatTransaction(accountManager, () -> accountManager.findByLogin(loginDto.getLogin()));
    } catch (ApplicationExceptionEntityNotFound e) {
      throw AuthApplicationException.createInvalidLoginOrPasswordException();
    }

    if (!account.getConfirmed()) {
        throw AccountApplicationException.createAccountNotConfirmedException();
    }

    UsernamePasswordCredential credential =
        new UsernamePasswordCredential(loginDto.getLogin(), loginDto.getPassword());
    CredentialValidationResult result = identityStoreHandler.validate(credential);
    if (result.getStatus() != CredentialValidationResult.Status.VALID) {
      repeatTransactionVoid(
          accountManager,
          () ->
              accountManager.updateAuthInformation(
                  credential.getCaller(),
                  httpServletRequest.getRemoteAddr(),
                  Date.from(Instant.now()),
                  false));
      throw AuthApplicationException.createInvalidLoginOrPasswordException();
    }

    repeatTransactionVoid(
        accountManager,
        () ->
            accountManager.updateAuthInformation(
                credential.getCaller(),
                httpServletRequest.getRemoteAddr(),
                Date.from(Instant.now()),
                true));
    return Response.ok(new TokenDto(jwtUtils.create(result))).build();
  }

  @POST
  @Path("/notify-access-level-change/{role}")
  @RolesAllowed("notifyAccessLevelChange")
  public Response notifyAccessLevelChange(@PathParam("role") String role) {
    Account account = repeatTransaction(accountManager, () -> accountManager.getCurrentUserWithAccessLevels());
    Role roleEnum;
    try {
      roleEnum = Role.valueOf(role);
    } catch(IllegalArgumentException e) {
      throw AccountApplicationException.createUndefinedAccessLevelException();
    }
    try {
      AccessLevelFinder.findAccessLevel(account, roleEnum);
    } catch(ApplicationException e) {
      log.info(String.format("User %s tried to change role to %s, which they do not have",
              accountManager.getCurrentUserLogin(), role));
      throw ApplicationException.createUnauthorisedException();
    }
    log.info(String.format("User %s changed role to %s",
            accountManager.getCurrentUserLogin(), role));
    return Response.ok().build();
  }
}
