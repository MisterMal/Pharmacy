package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;

import lombok.extern.java.Log;
import org.apache.commons.codec.language.bm.Languages;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.config.ETagFilterBinding;
import pl.lodz.p.it.ssbd2023.ssbd01.config.EntityIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.NewPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.VerificationTokenDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel.EditSelfAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel.EditSelfChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel.EditSelfPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccessLevelConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;

@Path("account")
@RequestScoped
@DenyAll
@Log
public class AccountController extends AbstractController {

  @Inject private AccountManagerLocal accountManager;

  @Inject private EntityIdentitySignerVerifier entityIdentitySignerVerifier;

  @GET
  @Path("/")
  @RolesAllowed("getAllAccounts")
  @Produces(MediaType.APPLICATION_JSON)
  public List<AccountDTO> readAllClients() {
    List<Account> accounts =
        repeatTransaction(accountManager, () -> accountManager.getAllAccounts());
    return accounts.stream().map(AccountConverter::mapAccountToAccountDto).toList();
  }

  @POST
  @Path("/confirm/{token}")
  @PermitAll
  public Response confirmAccount(@PathParam("token") String token) {
    repeatTransactionVoid(
            accountManager, () -> accountManager.confirmAccountRegistration(token));
    return Response.ok().build();
  }

  @POST
  @Path("/confirm-email-change")
  @RolesAllowed("confirmEmailChange")
  public Response confirmEmailChange(@Valid VerificationTokenDto token) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.confirmEmailChange(token.getToken()));
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  @RolesAllowed("getAccount")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readAccount(@PathParam("id") Long id) {
    Account account = repeatTransaction(accountManager, () -> accountManager.getAccount(id));
    AccountDTO accountDto = AccountConverter.mapAccountToAccountDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @GET
  @Path("/details")
  @RolesAllowed("getCurrentUserWithAccessLevels")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readOwnAccount() {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.getCurrentUserWithAccessLevels());
    SelfAccountWithAccessLevelDTO accountDto =
        AccountConverter.mapAccountToSelfAccountWithAccessLevelsDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @GET
  @Path("/{id}/details")
  @RolesAllowed("getAccountAndAccessLevels")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readAccountAndAccessLevels(@PathParam("id") Long id) {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.getAccountAndAccessLevels(id));
    AccountAndAccessLevelsDTO accountDto =
        AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @POST
  @Path("/register")
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerPatientAccount(@NotNull @Valid RegisterPatientDTO registerPatientDto) {
    Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
    repeatTransaction(accountManager, () -> accountManager.registerAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Path("/{id}/block")
  @RolesAllowed("blockAccount")
  public Response blockAccount(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.blockAccount(id));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{id}/unblock")
  @RolesAllowed("unblockAccount")
  public Response unblockAccount(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.unblockAccount(id));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("{id}/activate")
  @RolesAllowed("activateAccessLevel")
  public AccountDTO activateAccount(@PathParam("id") Long id) {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.activateUserAccount(id));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/{id}/change-user-password")
  @RolesAllowed("updateUserPassword")
  @ETagFilterBinding
  public AccountDTO changeUserPassword(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid UpdateOtherUserPasswordDTO passwordDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(passwordDTO, etag);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.updateUserPassword(
                id, passwordDTO.getPassword(), passwordDTO.getLogin(), passwordDTO.getVersion()));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/change-password")
  @RolesAllowed("updateOwnPassword")
  @ETagFilterBinding
  public Response changePassword(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @Valid ChangePasswordDTO changePasswordDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(changePasswordDTO, etag);
    repeatTransaction(accountManager,
        () -> accountManager.updateOwnPassword(
                changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword(),
                changePasswordDTO.getLogin(), changePasswordDTO.getVersion()));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/")
  @RolesAllowed("updateOwnEmail")
  @ETagFilterBinding
  public AccountDTO editOwnAccount(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @Valid EditAccountDTO editAccountDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(editAccountDTO, etag);
    Account account = repeatTransaction(accountManager,
            () -> accountManager.updateOwnEmail(editAccountDTO.getEmail(),
                    editAccountDTO.getVersion()));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/change-language")
  @RolesAllowed("changeAccountLanguage")
  public Response changeLanguage(@QueryParam("language") String language) {
    repeatTransactionVoid(accountManager,
            () -> accountManager.changeAccountLanguage(language));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}")
  @RolesAllowed("updateUserEmail")
  @ETagFilterBinding
  public AccountDTO editUserAccount(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid EditAccountDTO editAccountDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(editAccountDTO, etag);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.updateUserEmail(
                id, editAccountDTO.getEmail(), editAccountDTO.getLogin(), editAccountDTO.getVersion()));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/reset-password")
  @PermitAll
  public Response resetPassword(@Valid ResetPasswordDTO resetPasswordDTO) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.sendResetPasswordToken(resetPasswordDTO.getEmail()));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/new-password")
  @PermitAll
  public Response setNewPassword(@Valid NewPasswordDTO newPasswordDTO) {
    repeatTransactionVoid(
        accountManager,
        () ->
            accountManager.setNewPassword(newPasswordDTO.getToken(), newPasswordDTO.getPassword()));
    return Response.status(Response.Status.OK).build();
  }

  @POST
  @Path("/add-patient")
  @RolesAllowed("createAccount")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPatientAccountAsAdmin(
      @NotNull @Valid AddPatientAccountDto addPatientAccountDto) {
    Account account = AccountConverter.mapAddPatientDtoToAccount(addPatientAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-chemist")
  @RolesAllowed("createAccount")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addChemistAccountAsAdmin(
      @NotNull @Valid AddChemistAccountDto addChemistAccountDto) {
    Account account = AccountConverter.mapChemistDtoToAccount(addChemistAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-admin")
  @RolesAllowed("createAccount")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addAdminAccountAsAdmin(@NotNull @Valid AddAdminAccountDto addAdminAccountDto) {
    Account account = AccountConverter.mapAdminDtoToAccount(addAdminAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  // append access level
  @POST
  @Path("/{id}/patient")
  @RolesAllowed("grantAccessLevel")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantPatient(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantPatientDataDTO patientDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(patientDataDTO, etag);
    PatientData patientData =
        AccessLevelConverter.mapGrantPatientDataDTOtoPatientData(patientDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(
                id, patientData, patientDataDTO.getLogin(), patientDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/{id}/chemist")
  @RolesAllowed("grantAccessLevel")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantChemist(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantChemistDataDTO chemistDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(chemistDataDTO, etag);
    ChemistData chemistData =
        AccessLevelConverter.mapGrantChemistDataDtoToChemistData(chemistDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(
                id, chemistData, chemistDataDTO.getLogin(), chemistDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/{id}/admin")
  @RolesAllowed("grantAccessLevel")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantAdmin(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantAdminDataDTO adminDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(adminDataDTO, etag);
    AdminData adminData = AccessLevelConverter.mapGrantAdminDataDtoToAdminData(adminDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(
                id, adminData, adminDataDTO.getLogin(), adminDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  // read access level
  @GET
  @Path("/{id}/patient")
  @RolesAllowed("getAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPatientData(@PathParam("id") Long id) {
    AccessLevel accessLevel = repeatTransaction(
            accountManager, () -> accountManager.getAccessLevel(id, Role.PATIENT));
    AccessLevelDTO accessLevelDTO = AccessLevelConverter.mapAccessLevelToAccessLevelDto(accessLevel);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accessLevelDTO);
    return Response.status(Response.Status.OK).entity(accessLevelDTO).tag(etag).build();
  }

  @GET
  @Path("/{id}/chemist")
  @RolesAllowed("getAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getChemistData(@PathParam("id") Long id) {
    AccessLevel accessLevel = repeatTransaction(
            accountManager, () -> accountManager.getAccessLevel(id, Role.CHEMIST));
    AccessLevelDTO accessLevelDTO = AccessLevelConverter.mapAccessLevelToAccessLevelDto(accessLevel);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accessLevelDTO);
    return Response.status(Response.Status.OK).entity(accessLevelDTO).tag(etag).build();
  }

  @GET
  @Path("/{id}/admin")
  @RolesAllowed("getAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAdminData(@PathParam("id") Long id) {
    AccessLevel accessLevel = repeatTransaction(
            accountManager, () -> accountManager.getAccessLevel(id, Role.ADMIN));
    AccessLevelDTO accessLevelDTO = AccessLevelConverter.mapAccessLevelToAccessLevelDto(accessLevel);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accessLevelDTO);
    return Response.status(Response.Status.OK).entity(accessLevelDTO).tag(etag).build();
  }

  // edit access level
  @PUT
  @Path("/{id}/patient")
  @RolesAllowed("editAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editPatientData(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @PathParam("id") Long id,
          @Valid EditPatientDataDTO patientDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(patientDataDTO, etag);
    PatientData patientData =
            AccessLevelConverter.mapEditPatientDataDtoToPatientData(patientDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editAccessLevel(id, patientData, patientDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/chemist")
  @RolesAllowed("editAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editChemistData(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @PathParam("id") Long id,
          @Valid EditChemistDataDTO chemistDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(chemistDataDTO, etag);
    ChemistData chemistData =
            AccessLevelConverter.mapEditChemistDataDtoToChemistData(chemistDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editAccessLevel(id, chemistData, chemistDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/admin")
  @RolesAllowed("editAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editAdminData(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @PathParam("id") Long id,
          @Valid EditAdminDataDTO adminDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(adminDataDTO, etag);
    AdminData adminData = AccessLevelConverter.mapEditAdminDataDtoToAdminData(adminDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editAccessLevel(id, adminData, adminDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/patient")
  @RolesAllowed("editSelfAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editPatientDataSelf(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @Valid EditSelfPatientDataDTO patientDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(patientDataDTO, etag);
    PatientData patientData =
            AccessLevelConverter.mapEditSelfPatientDataDtoToPatientData(patientDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editSelfAccessLevel(patientData, patientDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/chemist")
  @RolesAllowed("editSelfAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editChemistDataSelf(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @Valid EditSelfChemistDataDTO chemistDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(chemistDataDTO, etag);
    ChemistData chemistData =
            AccessLevelConverter.mapEditSelfChemistDataDtoToChemistData(chemistDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editSelfAccessLevel(chemistData, chemistDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/admin")
  @RolesAllowed("editSelfAccessLevel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editAdminDataSelf(
          @HeaderParam("If-Match") @NotEmpty String etag,
          @Valid EditSelfAdminDataDTO adminDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(adminDataDTO, etag);
    AdminData adminData = AccessLevelConverter.mapEditSelfAdminDataDtoToAdminData(adminDataDTO);
    Account account =
            repeatTransaction(
                    accountManager,
                    () -> accountManager.editSelfAccessLevel(adminData, adminDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }



  @PUT
  @Path("/{id}/admin/block")
  @RolesAllowed("deactivateAccessLevel")
  public Response blockRoleAdmin(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.ADMIN));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/chemist/block")
  @RolesAllowed("deactivateAccessLevel")
  public Response blockRoleChemist(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.CHEMIST));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/patient/block")
  @RolesAllowed("deactivateAccessLevel")
  public Response blockRolePatient(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.PATIENT));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/admin/unblock")
  @RolesAllowed("activateAccessLevel")
  public Response unblockRoleAdmin(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.activateAccessLevel(id, Role.ADMIN));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/chemist/unblock")
  @RolesAllowed("activateAccessLevel")
  public Response unblockRoleChemist(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.activateAccessLevel(id, Role.CHEMIST));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/patient/unblock")
  @RolesAllowed("activateAccessLevel")
  public Response unblockRolePatient(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.activateAccessLevel(id, Role.PATIENT));
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
