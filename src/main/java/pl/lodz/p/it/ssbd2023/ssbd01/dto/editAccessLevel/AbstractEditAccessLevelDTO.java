package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEditAccessLevelDTO implements SignableEntity {

    @NotNull
    private Long version;

    @NotNull
    private Role role;

    @Override
    @JsonIgnore
    public String getSignablePayload() {
        return String.format("%s.%d", role, version);
    }
}
