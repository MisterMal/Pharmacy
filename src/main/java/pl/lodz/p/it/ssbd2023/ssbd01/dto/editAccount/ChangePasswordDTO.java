package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO extends AbstractEditAccountDTO {

  @Builder
  public ChangePasswordDTO(String login, Long version, String oldPassword, String newPassword) {
    super(login, version);
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  @NotNull
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password")
  @Size(max = 50, min = 8)
  String oldPassword;

  @NotNull
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password")
  @Size(max = 50, min = 8)
  String newPassword;
}
