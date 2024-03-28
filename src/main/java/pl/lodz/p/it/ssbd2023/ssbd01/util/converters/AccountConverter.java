package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import java.util.Locale;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountAndAccessLevelsDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.SelfAccountWithAccessLevelDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

public class AccountConverter {

  private AccountConverter() {}

  public static AccountDTO mapAccountToAccountDto(Account account) {
    return AccountDTO.builder()
        .id(account.getId())
        .version(account.getVersion())
        .confirmed(account.getConfirmed())
        .active(account.getActive())
        .login(account.getLogin())
        .email(account.getEmail())
        .build();
  }

  public static AccountAndAccessLevelsDTO mapAccountToAccountAndAccessLevelsDto(Account account) {
    var accessLevels =
        AccessLevelConverter.mapAccessLevelsToAccessLevelsDto(account.getAccessLevels());
    return AccountAndAccessLevelsDTO.accountAndAccessLevelsBuilder()
        .id(account.getId())
        .version(account.getVersion())
        .accessLevels(accessLevels)
        .login(account.getLogin())
        .email(account.getEmail())
        .active(account.getActive())
        .confirmed(account.getConfirmed())
        .build();
  }

  public static SelfAccountWithAccessLevelDTO mapAccountToSelfAccountWithAccessLevelsDto(Account account) {
    var accessLevels =
            AccessLevelConverter.mapAccessLevelsToAccessLevelsDto(account.getAccessLevels());
    return SelfAccountWithAccessLevelDTO.childBuilder()
            .id(account.getId())
            .version(account.getVersion())
            .accessLevels(accessLevels)
            .login(account.getLogin())
            .active(account.getActive())
            .confirmed(account.getConfirmed())
            .email(account.getEmail())
            .language(account.getLanguage())
            .build();
  }

  public static Account mapRegisterPatientDtoToAccount(RegisterPatientDTO registerPatientDto) {

    Account account =
        Account.builder()
            .login(registerPatientDto.getLogin())
            .password(registerPatientDto.getPassword())
            .build();
    account.setEmail(registerPatientDto.getEmail());
    account.setActive(true);
    account.setConfirmed(false);
    account.setLanguage(
        registerPatientDto.getLanguage() == null
            ? Locale.forLanguageTag("pl")
            : Locale.forLanguageTag(registerPatientDto.getLanguage()));

    PatientData patientData =
        PatientData.builder()
            .firstName(registerPatientDto.getName())
            .lastName(registerPatientDto.getLastName())
            .pesel(registerPatientDto.getPesel())
            .phoneNumber(registerPatientDto.getPhoneNumber())
            .NIP(registerPatientDto.getNip())
            .build();

    account.getAccessLevels().add(patientData);
    patientData.setAccount(account);

    return account;
  }

  public static Account mapAddPatientDtoToAccount(AddPatientAccountDto addPatientAccountDto) {

    Account account =
        Account.builder()
            .login(addPatientAccountDto.getLogin())
            .password(addPatientAccountDto.getPassword())
            .build();
    account.setEmail(addPatientAccountDto.getEmail());
    account.setActive(true);
    account.setConfirmed(true);
    account.setLanguage(
        addPatientAccountDto.getLanguage() == null
            ? Locale.forLanguageTag("pl")
            : Locale.forLanguageTag(addPatientAccountDto.getLanguage()));

    PatientData patientData =
        PatientData.builder()
            .firstName(addPatientAccountDto.getName())
            .lastName(addPatientAccountDto.getLastName())
            .pesel(addPatientAccountDto.getPesel())
            .phoneNumber(addPatientAccountDto.getPhoneNumber())
            .NIP(addPatientAccountDto.getNip())
            .build();

    account.getAccessLevels().add(patientData);
    patientData.setAccount(account);

    return account;
  }

  public static Account mapChemistDtoToAccount(AddChemistAccountDto addChemistAccountDto) {

    Account account =
        Account.builder()
            .login(addChemistAccountDto.getLogin())
            .password(addChemistAccountDto.getPassword())
            .build();
    account.setEmail(addChemistAccountDto.getEmail());
    account.setActive(true);
    account.setConfirmed(true);
    account.setLanguage(
        addChemistAccountDto.getLanguage() == null
            ? Locale.forLanguageTag("pl")
            : Locale.forLanguageTag(addChemistAccountDto.getLanguage()));

    ChemistData chemistData =
        ChemistData.builder().licenseNumber(addChemistAccountDto.getLicenseNumber()).build();

    account.getAccessLevels().add(chemistData);
    chemistData.setAccount(account);

    return account;
  }

  public static Account mapAdminDtoToAccount(AddAdminAccountDto addAdminAccountDto) {

    Account account =
        Account.builder()
            .login(addAdminAccountDto.getLogin())
            .password(addAdminAccountDto.getPassword())
            .build();
    account.setEmail(addAdminAccountDto.getEmail());
    account.setActive(true);
    account.setConfirmed(true);
    account.setLanguage(
        addAdminAccountDto.getLanguage() == null
            ? Locale.forLanguageTag("pl")
            : Locale.forLanguageTag(addAdminAccountDto.getLanguage()));

    AdminData adminData = new AdminData();
    adminData.setWorkPhoneNumber(addAdminAccountDto.getWorkPhoneNumber());

    account.getAccessLevels().add(adminData);
    adminData.setAccount(account);

    return account;
  }
}
