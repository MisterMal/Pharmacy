package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class AdminDataDTO extends AccessLevelDTO {

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;

  @Builder
  public AdminDataDTO(Long id, Long version, Role role, Boolean active, String workPhoneNumber) {
    super(id, version, role, active);
    this.workPhoneNumber = workPhoneNumber;
  }
}
