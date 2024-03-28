package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotNull
    private String orderDate;

    @NotNull
    private List<CreateOrderMedicationDTO> orderMedications;

    private CreateOrderPrescriptionDTO prescription;
}
