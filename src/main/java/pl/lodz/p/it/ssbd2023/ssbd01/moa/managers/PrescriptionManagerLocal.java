package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

import java.util.List;

public interface PrescriptionManagerLocal extends CommonManagerLocalInterface {

    Prescription createPrescription(Prescription prescription);

    Prescription getPrescription(Long id);

    Prescription editPrescription(Prescription prescription);

    List<Prescription> getAllPrescriptions();
}
