package pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditSelfChemistDataDTO extends AbstractEditSelfAccessLevel {

  @Builder
  public EditSelfChemistDataDTO(Long version, String login, String licenseNumber) {
    super(version, login);
    this.licenseNumber = licenseNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{6}$", message = "Invalid license number")
  private String licenseNumber;
}
