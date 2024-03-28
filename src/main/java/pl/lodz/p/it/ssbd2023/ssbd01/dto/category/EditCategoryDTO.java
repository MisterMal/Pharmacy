package pl.lodz.p.it.ssbd2023.ssbd01.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditCategoryDTO implements SignableEntity {

    @NotNull
    private Long id;

    @NotNull
    private Long version;

    @NotNull
    @Size(max = 50, min = 3)
    private String name;

    @NotNull
    private Boolean isOnPrescription;

    @Override
    public String getSignablePayload() {
        return String.format("%d.%d", id, version);
    }
}
