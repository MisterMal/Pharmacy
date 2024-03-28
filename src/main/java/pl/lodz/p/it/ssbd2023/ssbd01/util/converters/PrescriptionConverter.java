package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.prescrription.PrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

public class PrescriptionConverter {

  private PrescriptionConverter() {}

  public static PrescriptionDTO mapPrescriptionToPrescriptionDTO(Prescription prescription) {
    return PrescriptionDTO.builder()
        .prescriptionNumber(prescription.getPrescriptionNumber())
        .build();
  }
}
