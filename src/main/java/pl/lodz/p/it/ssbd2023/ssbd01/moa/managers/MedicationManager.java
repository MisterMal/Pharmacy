package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;

import java.util.List;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class,
        TrackerInterceptor.class})
@Log
@DenyAll
public class MedicationManager extends AbstractManager implements MedicationManagerLocal, SessionSynchronization {

    @Inject
    private MedicationFacade medicationFacade;

    @Inject
    private CategoryFacade categoryFacade;

    @Inject
    private AccountFacade accountFacade;

    @Context
    private SecurityContext context;

    @Override
    @PermitAll
    public Medication findByName(String name) {
        return medicationFacade.findByName(name);
    }

    @Override
    @RolesAllowed("createMedication")
    public Medication createMedication(Medication medication, String categoryName) {
        Category managedCategory = categoryFacade.findByName(categoryName);
        medication.setCategory(managedCategory);
        medication.setCreatedBy(getCurrentUserLogin());
        medicationFacade.create(medication);
        return medication;
    }

    @Override
    @RolesAllowed("getAllMedications")
    public List<Medication> getAllMedications() {
        return medicationFacade.findAll();
    }

    @Override
    @PermitAll
    public Medication getMedication(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Medication editMedication(Medication medication) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("getMedicationDetails")
    public Medication getMedicationDetails(Long id) {
        Optional<Medication> medication = medicationFacade.findAndRefresh(id);
        if (medication.isEmpty()) {
            throw ApplicationException.createEntityNotFoundException();
        }
        return medication.get();
    }

    @RolesAllowed("getCurrentUser")
    public Account getCurrentUser() {
        return accountFacade.findByLogin(getCurrentUserLogin());
    }

    @PermitAll
    public String getCurrentUserLogin() {
        return context.getUserPrincipal().getName();
    }

}
