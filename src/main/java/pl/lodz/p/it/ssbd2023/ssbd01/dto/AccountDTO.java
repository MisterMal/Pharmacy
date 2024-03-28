package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO extends AbstractEntityDTO implements SignableEntity {

  @Builder
  public AccountDTO(
      Long id, Long version, String login, Boolean active, Boolean confirmed, String email) {
    super(id, version);
    this.login = login;
    this.active = active;
    this.confirmed = confirmed;
    this.email = email;
  }

  @Size(max = 50, min = 5)
  @NotNull
  private String login;

  @Size(max = 50, min = 5)
  @Email
  @NotNull
  private String email;

  @NotNull
  private Boolean active;

  @NotNull
  private Boolean confirmed;

  @Override
  @JsonIgnore
  public String getSignablePayload() {
    return String.format("%s.%d", login, getVersion());
  }
}
