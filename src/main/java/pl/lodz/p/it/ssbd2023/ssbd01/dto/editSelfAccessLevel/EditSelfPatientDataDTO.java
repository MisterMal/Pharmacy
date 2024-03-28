package pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.AbstractEditAccessLevelDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditSelfPatientDataDTO extends AbstractEditSelfAccessLevel {

  @Builder
  public EditSelfPatientDataDTO(Long version, String login, String pesel, String firstName,
                                String lastName, String phoneNumber, String nip) {
    super(version, login);
    this.pesel = pesel;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.nip = nip;
  }

  @NotNull
  @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
  private String pesel;

  @NotNull
  @Size(max = 50, min = 2)
  private String firstName;

  @NotNull
  @Size(max = 50, min = 2)
  private String lastName;

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "^\\d{10}$", message = "Invalid NIP")
  private String nip;
}
