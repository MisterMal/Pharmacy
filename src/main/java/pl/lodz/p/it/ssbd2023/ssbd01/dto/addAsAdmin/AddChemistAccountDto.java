package pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.BasicAccountDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddChemistAccountDto extends BasicAccountDto {

  @NotNull
  @Pattern(regexp = "^\\d{6}$", message = "Invalid license number")
  private String licenseNumber;

  @Builder
  public AddChemistAccountDto(
      String login,
      String password,
      String email,
      String language,
      String licenseNumber) {
    super(login, password, email, language);
    this.licenseNumber = licenseNumber;
  }
}
