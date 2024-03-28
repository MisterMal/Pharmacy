package pl.lodz.p.it.ssbd2023.ssbd01.dto.category;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;

@Data
@NoArgsConstructor
public class CategoryDTO {

    @NotNull
    @Size(max = 50, min = 2)
    private String name;

    //@NotNull
    private Boolean isOnPrescription;

    @Builder
    public CategoryDTO(String name, Boolean isOnPrescription) {
        this.name = name;
        this.isOnPrescription = isOnPrescription;
    }
}
