package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

public class MedicationConverter {

  private MedicationConverter() {}

  public static MedicationDTO mapMedicationToMedicationDTO(Medication medication) {
    return MedicationDTO.medicationDTOBuilder()
            .id(medication.getId())
            .version(medication.getVersion())
            .name(medication.getName())
            .stock(medication.getStock())
            .currentPrice(medication.getCurrentPrice())
            .categoryDTO(
                    CategoryConverter.mapCategoryToCategoryDTO(medication.getCategory()))
            .build();
  }

  public static Medication mapMedicationDtoToMedication(MedicationDTO medicationDTO) {
        return null;
    }

}
