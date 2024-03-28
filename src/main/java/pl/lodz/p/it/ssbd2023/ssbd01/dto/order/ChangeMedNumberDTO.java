package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class ChangeMedNumberDTO {

    @NotNull
    Long medicationId;

    @NotNull
    Integer quantity;

    @Builder
    ChangeMedNumberDTO(Long medicationId, Integer quantity) {
        this.medicationId = medicationId;
        this.quantity = quantity;
    }
}

