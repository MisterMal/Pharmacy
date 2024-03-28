package pl.lodz.p.it.ssbd2023.ssbd01.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;


@Getter
@Setter
@NoArgsConstructor
public class GetCategoryDTO extends AbstractEntityDTO implements SignableEntity {

    @NotNull
    private String name;

    @NotNull
    private Boolean isOnPrescription;

    @Builder
    public GetCategoryDTO(Long id, Long version, String name, Boolean isOnPrescription) {
        super(id, version);
        this.name = name;
        this.isOnPrescription = isOnPrescription;
    }

    @Override
    public String getSignablePayload() {
        return String.format("%s.%d", getId(), getVersion());
    }
}
