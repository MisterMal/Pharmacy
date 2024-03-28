package controller;

import java.math.BigDecimal;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.EditCategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.AddMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;

public class dataForTests {

  public static LoginDTO adminLoginDto = new LoginDTO("admin123", "P@ssw0rd");
  public static LoginDTO patientRegisteredLoginDto = new LoginDTO("test11", "testP@tient123");
  public static LoginDTO patientLoginDto = new LoginDTO("patient123", "P@ssw0rd");

  public static LoginDTO chemistLoginDto = new LoginDTO("chemist123", "P4$$w0Rd");

  // register
  public static RegisterPatientDTO registerPatientDto =
      RegisterPatientDTO.builder()
          .login(patientRegisteredLoginDto.getLogin())
          .password(patientRegisteredLoginDto.getPassword())
          .email("patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateLogin =
      RegisterPatientDTO.builder()
          .login(patientRegisteredLoginDto.getLogin())
          .password(patientRegisteredLoginDto.getPassword())
          .email("other-patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateEmail =
      RegisterPatientDTO.builder()
          .login("other-login")
          .password(patientRegisteredLoginDto.getPassword())
          .email("patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicatePesel =
      RegisterPatientDTO.builder()
          .login("pesel-login")
          .password(patientRegisteredLoginDto.getPassword())
          .email("pesel-patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123321")
          .pesel("12345678901")
          .nip("4443332213")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateNip =
      RegisterPatientDTO.builder()
          .login("nip-login")
          .password(patientRegisteredLoginDto.getPassword())
          .email("nip-patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123321")
          .pesel("12345678999")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicatePhoneNumber =
      RegisterPatientDTO.builder()
          .login("phone-login")
          .password(patientRegisteredLoginDto.getPassword())
          .email("phone-patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678910")
          .nip("1443332211")
          .build();
  // grant
  public static GrantChemistDataDTO grantChemistDataDTO =
      GrantChemistDataDTO.builder()
          .login(patientRegisteredLoginDto.getLogin())
          .licenseNumber("127836")
          .version(0L)
          .build();

  public static GrantAdminDataDTO grantAdminDataDTO =
      GrantAdminDataDTO.builder()
          .login(patientRegisteredLoginDto.getLogin())
          .workPhoneNumber("123431431")
          .version(0L)
          .build();

  public static GrantPatientDataDTO grantPatientDataDTO =
          GrantPatientDataDTO.builder()
                  .login(chemistLoginDto.getLogin())
                  .version(0L)
                  .pesel("12345698712")
                  .firstName("Bob")
                  .lastName("Robertson")
                  .phoneNumber("982737022")
                  .nip("9346105462")
                  .build();

  // edit data
  public static EditAdminDataDTO adminDataDTOChangedPhone =
      EditAdminDataDTO.builder().version(0L).workPhoneNumber("102938129").build();

  public static EditChemistDataDTO chemistDataDTOChangedLiscence =
      EditChemistDataDTO.builder().version(0L).licenseNumber("412312").build();

  public static EditPatientDataDTO patientDataDTOChangedName =
      EditPatientDataDTO.builder()
          .version(0L)
          .pesel(registerPatientDto.getPesel())
          .firstName("Othername")
          .lastName(registerPatientDto.getLastName())
          .phoneNumber(registerPatientDto.getPhoneNumber())
          .nip(registerPatientDto.getNip())
          .build();

  // add account
  public static AddPatientAccountDto addPatientAccountDto =
          AddPatientAccountDto.builder()
                  .login("testPatient")
                  .password("testCh3m!st")
                  .email("testPatient@local")
                  .name("Pat")
                  .lastName("Postman")
                  .phoneNumber("123874094")
                  .pesel("12387650987")
                  .nip("7254973540")
                  .build();
  public static AddChemistAccountDto addChemistAccountDto =
      AddChemistAccountDto.builder()
          .login("testChemist")
          .password("testCh3m!st")
          .email("testChemist@o2.pl")
          .licenseNumber("123456")
          .build();

  public static AddAdminAccountDto addAdminAccountDto =
      AddAdminAccountDto.builder()
          .login("testAdmin")
          .password("test@Dm1n")
          .email("testAdmin@o2.pl")
          .workPhoneNumber("123426123")
          .build();

  public static AddChemistAccountDto addChemistAccountDtoMissingField =
      AddChemistAccountDto.builder()
          .login("incorrectChemist")
          .password("incorrectCh3m!st")
          .email("incorrectChemist@o2.pl")
          .build();

  public static AddAdminAccountDto addAdminAccountDtoMissingField =
      AddAdminAccountDto.builder()
          .login("incorrectAdmin")
          .password("incorrect@Dm1n")
          .email("incorrectAdmin@o2.pl")
          .build();

  public static EditAccountDTO editEmailDto = new EditAccountDTO("new@email.local");

  public static CategoryDTO categoryDto =
      CategoryDTO.builder().name("NajnowszaKategoria").isOnPrescription(false).build();

  public static AddMedicationDTO addMedicationDto =
      AddMedicationDTO.builder()
          .name("Pyszny lek")
          .stock(100)
          .price(new BigDecimal("10.0"))
          .categoryName(categoryDto.getName())
          .build();
//        CategoryDTO.CategoryDTOBuilder().name("Antydepresanty").isOnPrescription(false).build();

  public static AddMedicationDTO addMedicationDtoWrongCategory =
      AddMedicationDTO.builder()
              .name("Pyszny lek")
              .stock(100)
              .price(new BigDecimal("10.0"))
              .categoryName("Meh")
              .build();

  public static MedicationDTO medicationDetailsDto =
      MedicationDTO.medicationDTOBuilder()
          .name("Prozac")
          .stock(100)
          .currentPrice(new BigDecimal("10.0"))
          .categoryDTO(categoryDto)
          .build();

  public static CreateOrderMedicationDTO createOrderMedicationDTO =
          CreateOrderMedicationDTO.builder()
                  .quantity(1)
                  .name("Prozac")
                  .build();

  public static EditCategoryDTO editCategoryDTO =
          EditCategoryDTO.builder()
                  .id(4L)
                  .name("Edited")
                  .isOnPrescription(false)
                  .version(1L)
                  .build();
}
