package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AddMedicationDTO {

    @NotNull
    @Size(max = 50, min = 2)
    private String name;

    @NotNull
    private Integer stock;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String categoryName;

    @Builder
    public AddMedicationDTO(String name, Integer stock, BigDecimal price, String categoryName) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.categoryName = categoryName;
    }
}
