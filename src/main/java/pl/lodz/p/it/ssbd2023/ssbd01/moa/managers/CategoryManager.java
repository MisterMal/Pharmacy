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
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@Stateful
@DenyAll
public class CategoryManager extends AbstractManager implements CategoryManagerLocal, SessionSynchronization {


    @Inject
    private CategoryFacade categoryFacade;

    @Inject
    private AccountFacade accountFacade;

    @Context
    private SecurityContext context;

    @Override
    @RolesAllowed("getAllCategories")
    public List<Category> getAllCategories() {
        return categoryFacade.findAll();    }

    @Override
    @RolesAllowed("createCategory")
    public Category createCategory(Category category) {
        Category existingCategory = categoryFacade.findByName(category.getName());
        if (existingCategory != null) {
            throw ApplicationException.createCategoryAlreadyExistsException();
        }
        category.setCreatedBy(getCurrentUserLogin());
        categoryFacade.create(category);
        return category;
    }

    @Override
    @PermitAll
    public Category getCategory(Long id) {
        Optional<Category> category = categoryFacade.find(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw ApplicationException.createEntityNotFoundException();
        }
    }

    @Override
    @RolesAllowed("editCategory")
    public Category editCategory(Long id, Category category, Long version) {
        Optional<Category> categoryOptional = categoryFacade.find(id);
        if (categoryOptional.isPresent()) {
            Category categoryToUpdate = categoryOptional.get();
            if (!Objects.equals(categoryToUpdate.getVersion(), version)) {
                throw ApplicationException.createOptimisticLockException();
            }
            categoryToUpdate.setName(category.getName());
            categoryToUpdate.setIsOnPrescription(category.getIsOnPrescription());
            categoryToUpdate.setModifiedBy(getCurrentUserLogin());
            categoryToUpdate.setModificationDate(new Date());
            categoryFacade.edit(categoryToUpdate);
            return categoryToUpdate;
        } else {
            throw ApplicationException.createEntityNotFoundException();
        }
    }

    @Override
    @PermitAll
    public Category findByName(String name) {
        return categoryFacade.findByName(name);
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
