package pl.lodz.p.it.ssbd2023.ssbd01.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFacade<T> {

  private final Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  protected void create(T entity) {
    getEntityManager().persist(entity);
    getEntityManager().flush();
  }

  protected void edit(T entity) {
    getEntityManager().merge(entity);
    getEntityManager().flush();
  }

  protected void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
    getEntityManager().flush();
  }

  protected Optional<T> find(Object id) {
    return Optional.ofNullable(getEntityManager().find(entityClass, id));
  }

  protected Optional<T> findAndRefresh(Object id) {
    Optional<T> optEntity = find(id);
    optEntity.ifPresent(t -> getEntityManager().refresh(t));
    getEntityManager().flush();
    return optEntity;
  }

  protected List<T> findAll() {
    CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }
}
