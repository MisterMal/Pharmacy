package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CreatePatientDTO {

  AccountDTO accountDTO;

  PatientDataDTO patientDataDTO;

  @ToString.Exclude
  @NotNull
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
  @Size(max = 50, min = 8)
  private String password;
}
