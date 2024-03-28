package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderMedicationConverter {

    private OrderMedicationConverter() {}

    public static OrderMedicationDTO mapOrderMedicationToOrderMedicationDTO(OrderMedication orderMedication) {
        return OrderMedicationDTO.builder()
                .medication(MedicationConverter.mapMedicationToMedicationDTO(orderMedication.getMedication()))
                .quantity(orderMedication.getQuantity())
                .purchasePrice(orderMedication.getPurchasePrice())
                .build();
    }
    public static List<OrderMedication> mapCreateOrderMedicationsDTOToOrderMedications(
            List<CreateOrderMedicationDTO> createOrderMedicationDTO) {
        return createOrderMedicationDTO == null ?
                null :
                createOrderMedicationDTO.stream()
                        .filter(Objects::nonNull)
                        .map(OrderMedicationConverter::mapCreateOrderMedDTOToOrderMed)
                        .collect(Collectors.toList());
    }
    public static OrderMedication mapCreateOrderMedDTOToOrderMed(CreateOrderMedicationDTO createOrderMedicationDTO) {
        return OrderMedication.builder()
                .medication(Medication.builder()
                        .name(createOrderMedicationDTO.getName())
                        .build())
                .quantity(createOrderMedicationDTO.getQuantity())
                .build();
    }
}

