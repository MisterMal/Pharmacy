package pl.lodz.p.it.ssbd2023.ssbd01.dto.editSelfAccessLevel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEditSelfAccessLevel implements SignableEntity {
    @NotNull
    private Long version;

    @NotNull
    private String login;

    @Override
    @JsonIgnore
    public String getSignablePayload() {
        return String.format("%s.%d", login, version);
    }
}
