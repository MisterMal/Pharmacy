package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountAndAccessLevelsDTO extends AccountDTO {

  @Builder(builderMethodName = "accountAndAccessLevelsBuilder")
  public AccountAndAccessLevelsDTO(
      Long id,
      Long version,
      Set<AccessLevelDTO> accessLevels,
      String login,
      String email,
      Boolean active,
      Boolean confirmed) {
    super(id, version, login, active, confirmed, email);
    this.accessLevels = accessLevels;
  }

  @ToString.Exclude Set<AccessLevelDTO> accessLevels;
}
