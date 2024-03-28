package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderPrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.MedicationCreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;

public class OrderConverter {

  private OrderConverter() {}

  public static OrderDTO mapOrderToOrderDTO(Order order) {
    return OrderDTO.builder()
        .orderMedication(
            order.getOrderMedications().stream()
                .map(OrderMedicationConverter::mapOrderMedicationToOrderMedicationDTO)
                .toList())
        .patientData(AccessLevelConverter
                .mapPatientDataToPatientDataDto((PatientData) order.getPatientData()))
        .prescription(
            order.getPrescription() == null
                ? null
                : PrescriptionConverter.mapPrescriptionToPrescriptionDTO(order.getPrescription()))
        .orderDate(order.getOrderDate())
        .orderState(order.getOrderState())
        .id(order.getId())
        .build();
  }

  public static Order mapCreateOrderDTOToOrder(CreateOrderDTO createOrderDTO) {
    return Order.createBuilder()
            .orderMedications(OrderMedicationConverter.mapCreateOrderMedicationsDTOToOrderMedications(
                    createOrderDTO.getOrderMedications()))
            .orderDate(Date.from(LocalDateTime.parse(createOrderDTO.getOrderDate())
                    .toInstant(ZoneOffset.UTC)))
            .prescription(mapCreateOrderPrescriptionToPrescription(
                    createOrderDTO.getPrescription()))
            .createBuild();
  }

  public static Prescription mapCreateOrderPrescriptionToPrescription(
          CreateOrderPrescriptionDTO prescription) {
    if(prescription == null) {
      return null;
    }
    if(prescription.getPrescriptionNumber() == null) {
      return null;
    }
    return new Prescription(prescription.getPrescriptionNumber());
  }
}




