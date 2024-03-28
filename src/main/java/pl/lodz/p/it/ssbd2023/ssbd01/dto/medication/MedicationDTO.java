package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MedicationDTO extends AbstractEntityDTO implements SignableEntity {

    @NotNull
    private String name;

    @NotNull
    private Integer stock;

    @NotNull
    private BigDecimal currentPrice;

    @NotNull
    private CategoryDTO categoryDTO;

    @Builder(builderMethodName = "medicationDTOBuilder")
    public MedicationDTO(Long id, Long version, String name, Integer stock, BigDecimal currentPrice, CategoryDTO categoryDTO) {
        super(id, version);
        this.name = name;
        this.stock = stock;
        this.currentPrice = currentPrice;
        this.categoryDTO = categoryDTO;
    }

    @Override
    @JsonIgnore
    public String getSignablePayload() {
        return String.format("%s.%d", name, getVersion());
    }
}
