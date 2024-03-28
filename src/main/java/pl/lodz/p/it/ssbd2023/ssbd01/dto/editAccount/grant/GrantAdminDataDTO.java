package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.AbstractEditAccountDTO;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantAdminDataDTO extends AbstractEditAccountDTO {

  @Builder
  public GrantAdminDataDTO(String login, Long version, String workPhoneNumber) {
    super(login, version);
    this.workPhoneNumber = workPhoneNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;
}
