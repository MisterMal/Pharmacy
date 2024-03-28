package pl.lodz.p.it.ssbd2023.ssbd01.dto.register;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAccountDto {

  @Size(max = 50, min = 5)
  @NotNull
  private String login;

  @NotNull
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
  @Size(max = 50, min = 8)
  private String password;

  @NotNull
  @Email
  @Size(max = 50, min = 5)
  private String email;

  private String language;
}
