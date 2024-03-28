package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.config.ETagFilterBinding;
import pl.lodz.p.it.ssbd2023.ssbd01.config.EntityIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.EditCategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.GetCategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel.EditPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.CategoryManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.CategoryConverter;

import java.util.List;

@Path("category")
@RequestScoped
@DenyAll
@Log
public class CategoryController extends AbstractController {

    @Inject
    private CategoryManagerLocal categoryManager;

    @Inject
    private EntityIdentitySignerVerifier entityIdentitySignerVerifier;

    //moa 22
    @GET
    @Path("/")
    @RolesAllowed("getAllCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GetCategoryDTO> getAllCategories() {
        List<Category> categories =
                repeatTransaction(categoryManager, () -> categoryManager.getAllCategories());
        return categories.stream().map(CategoryConverter::mapCategoryToGetCategoryDTO).toList();
    }


    @GET
    @Path("/{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("id") Long id) {
        Category category = repeatTransaction(categoryManager, () -> categoryManager.getCategory(id));
        GetCategoryDTO getCategoryDTO = CategoryConverter.mapCategoryToGetCategoryDTO(category);
        String etag = entityIdentitySignerVerifier.calculateEntitySignature(getCategoryDTO);
        return Response.ok(getCategoryDTO).tag(etag).build();
    }

    //moa 21
    @POST
    @Path("/add-category")
    @RolesAllowed("createCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(@NotNull @Valid CategoryDTO categoryDto) {
        Category category = CategoryConverter.mapCategoryDTOToCategory(categoryDto);
        Category createdCategory = repeatTransaction(categoryManager, () -> categoryManager.createCategory(category));
        CategoryDTO createdCategoryDto = CategoryConverter.mapCategoryToCategoryDTO(createdCategory);
        return Response.status(Response.Status.CREATED).entity(createdCategoryDto).build();
    }

    //moa 23
    @PUT
    @Path("/{id}/edit-category")
    @RolesAllowed("editCategory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ETagFilterBinding
    public CategoryDTO editCategory(@HeaderParam("If-Match") @NotEmpty String etag,
                                    @PathParam("id") Long id,
                                    @Valid EditCategoryDTO editCategoryDTO) {
        entityIdentitySignerVerifier.checkEtagIntegrity(editCategoryDTO, etag);
        Category category = CategoryConverter.mapEditCategoryDTOToCategory(editCategoryDTO);
        Category editedCategory = repeatTransaction(categoryManager, () -> categoryManager.editCategory(id, category, editCategoryDTO.getVersion()));
        return CategoryConverter.mapCategoryToCategoryDTO(editedCategory);
    }
}
