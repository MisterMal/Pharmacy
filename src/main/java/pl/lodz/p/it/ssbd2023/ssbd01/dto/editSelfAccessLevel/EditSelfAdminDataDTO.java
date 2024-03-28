package pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditSelfAdminDataDTO extends AbstractEditSelfAccessLevel {

  @Builder
  public EditSelfAdminDataDTO(Long version, String login, String workPhoneNumber) {
    super(version, login);
    this.workPhoneNumber = workPhoneNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;
}
