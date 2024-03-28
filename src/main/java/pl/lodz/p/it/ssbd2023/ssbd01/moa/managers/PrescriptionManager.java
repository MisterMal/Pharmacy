package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

import java.util.List;

public class PrescriptionManager extends AbstractManager implements PrescriptionManagerLocal, SessionSynchronization {
    @Override
    @DenyAll
    public Prescription createPrescription(Prescription prescription) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Prescription getPrescription(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Prescription editPrescription(Prescription prescription) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Prescription> getAllPrescriptions() {
        throw new UnsupportedOperationException();
    }
}
