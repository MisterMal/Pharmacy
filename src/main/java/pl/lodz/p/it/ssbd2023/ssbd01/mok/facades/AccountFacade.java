package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.event.Observes;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.AccountFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({
  GenericFacadeExceptionsInterceptor.class,
  AccountFacadeExceptionsInterceptor.class,
  TrackerInterceptor.class
})
@DenyAll
public class AccountFacade extends AbstractFacade<Account> {
  @PersistenceContext(unitName = "ssbd01mokPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AccountFacade() {
    super(Account.class);
  }

  @Override
  @PermitAll
  public List<Account> findAll() {
    return super.findAll();
  }

  @RolesAllowed("getAccountAndAccessLevels")
  public Optional<Account> findAndRefresh(Long id) {
    return super.findAndRefresh(id);
  }

  @PermitAll
  public Account findByLogin(String login) {
    TypedQuery<Account> tq = em.createNamedQuery("account.findByLogin", Account.class);
    tq.setParameter(1, login);
    return tq.getSingleResult();
  }

  @PermitAll
  public Account findByLoginAndRefresh(String login) {
    TypedQuery<Account> tq = em.createNamedQuery("account.findByLogin", Account.class);
    tq.setParameter(1, login);
    Account foundAccount = tq.getSingleResult();
    getEntityManager().refresh(foundAccount);
    getEntityManager().flush();
    return foundAccount;
  }

  @PermitAll
  public Account findByEmail(String email) {
    TypedQuery<Account> tq = em.createNamedQuery("account.findByEmail", Account.class);
    tq.setParameter(1, email);
    return tq.getSingleResult();
  }

  @PermitAll
  public List<Account> findNotConfirmed() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery cq = cb.createQuery();
    Root<Account> root = cq.from(Account.class);
    cq.select(root).where(cb.isFalse(root.get("confirmed")));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @PermitAll
  public List<Account> findNotActiveAndIncorrectLoginAttemptsEqual(int attempts) {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery cq = cb.createQuery();
    Root<Account> root = cq.from(Account.class);
    cq.select(root).where(cb.and(
            cb.isFalse(root.get("active")),
            cb.equal(root.get("incorrectLoginAttempts"), attempts)));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @Override
  @PermitAll
  public void edit(Account account) {
    super.edit(account);
  }

  @Override
  @PermitAll
  public void create(Account account) {
    super.create(account);
  }

  @Override
  @PermitAll
  public void remove(Account account) {
    super.remove(account);
  }

  @PermitAll
  public Optional<Account> find(Long id) {
    return super.find(id);
  }
}
