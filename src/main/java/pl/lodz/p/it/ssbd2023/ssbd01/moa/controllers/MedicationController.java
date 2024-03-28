package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.config.EntityIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.AddMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.CategoryManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.MedicationManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.MedicationConverter;

import java.util.List;

@Path("medication")
@RequestScoped
@DenyAll
@Log
public class MedicationController extends AbstractController {

    @Inject
    private MedicationManagerLocal medicationManager;

    @Inject
    private CategoryManagerLocal categoryManager;

    @Inject
    private EntityIdentitySignerVerifier entityIdentitySignerVerifier;


    //moa 1
    @GET
    @Path("/")
    @RolesAllowed("getAllMedications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MedicationDTO> getAllMedications() {
        List<Medication> medications =
                repeatTransaction(medicationManager, () -> medicationManager.getAllMedications());
        return medications.stream().map(MedicationConverter::mapMedicationToMedicationDTO).toList();
    }

    //chyba moa 19 ale może w shimpment
    @POST
    @Path("/add-medication")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMedication(@Valid AddMedicationDTO addMedicationDTO) {
        Medication medication =
                Medication.builder()
                        .name(addMedicationDTO.getName())
                        .stock(addMedicationDTO.getStock())
                        .currentPrice(addMedicationDTO.getPrice())
                        .build();

        repeatTransaction(medicationManager, () -> medicationManager.createMedication(medication,
                addMedicationDTO.getCategoryName()));
        return Response.status(Response.Status.CREATED).build();
    }

    //moa 20
    @PUT
    @Path("/{id}/edit-medication")
    @DenyAll
    public AddMedicationDTO editMedication(AddMedicationDTO addMedicationDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //moa 2???
    @GET
    @Path("/{id}")
    @RolesAllowed("getMedicationDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicationDetails(@PathParam("id") Long id) {
        Medication medication = repeatTransaction(medicationManager, () -> medicationManager.getMedicationDetails(id));
        MedicationDTO medicationDTO = MedicationConverter.mapMedicationToMedicationDTO(medication);
        String etag = entityIdentitySignerVerifier.calculateEntitySignature(medicationDTO);
        return Response.ok(medicationDTO).tag(etag).build();
    }

}