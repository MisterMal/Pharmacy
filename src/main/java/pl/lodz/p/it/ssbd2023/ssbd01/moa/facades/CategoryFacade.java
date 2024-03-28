package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.AccountFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

@Stateless(name = "CategoryFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@DenyAll
@Interceptors({
        GenericFacadeExceptionsInterceptor.class,
        TrackerInterceptor.class
})
public class CategoryFacade extends AbstractFacade<Category> {
  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CategoryFacade() {
    super(Category.class);
  }

  @Override
  @PermitAll
  public List<Category> findAll() {
    TypedQuery<Category> tq = em.createNamedQuery("category.findAll", Category.class);
    return tq.getResultList();
  }

  @Override
  @RolesAllowed("createCategory")
  public void create(Category category) {super.create(category);}

  @Override
  @PermitAll
  public void edit(Category category) {super.edit(category);}

  @PermitAll
  public Optional<Category> find(Long id) {return super.find(id);}

  @PermitAll
  public Category findByName(String name) {
    try {
      TypedQuery<Category> tq = em.createNamedQuery("category.findByName", Category.class);
      tq.setParameter(1, name);
      return tq.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }
}
