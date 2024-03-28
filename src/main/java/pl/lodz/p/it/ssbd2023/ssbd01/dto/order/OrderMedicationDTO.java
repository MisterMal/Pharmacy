package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMedicationDTO {

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull
    private MedicationDTO medication;

    @Digits(integer = 10, fraction = 2)
    @Min(value = 0, message = "Purchase price must be greater than or equal 0")
    private BigDecimal purchasePrice;


}
