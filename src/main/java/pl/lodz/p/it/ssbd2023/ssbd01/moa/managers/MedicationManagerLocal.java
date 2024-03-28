package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

import java.util.List;

@Local
public interface MedicationManagerLocal extends CommonManagerLocalInterface {

    @PermitAll
    Medication findByName(String name);

    Medication createMedication(Medication medication, String categoryName);

    List<Medication> getAllMedications();

    Medication getMedication(Long id);

    Medication editMedication(Medication medication);

    Medication getMedicationDetails(Long id);


}
