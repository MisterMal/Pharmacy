package pl.lodz.p.it.ssbd2023.ssbd01.dto.register;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPatientDTO extends BasicAccountDto {

  @NotNull
  @Size(max = 50, min = 2)
  private String name;

  @NotNull
  @Size(max = 50, min = 2)
  private String lastName;

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "^\\d{11}$", message = "Invalid license number")
  private String pesel;

  @NotNull
  @Pattern(regexp = "^\\d{10}$", message = "Invalid NIP")
  private String nip;

  @Builder
  public RegisterPatientDTO(
      @NotNull String login,
      @NotNull String password,
      @NotNull String email,
      String language,
      String name,
      String lastName,
      String phoneNumber,
      String pesel,
      String nip) {
    super(login, password, email, language);
    this.name = name;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.pesel = pesel;
    this.nip = nip;
  }
}
