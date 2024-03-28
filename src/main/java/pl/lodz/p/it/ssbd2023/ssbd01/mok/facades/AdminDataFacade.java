package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;

@Stateless
public class AdminDataFacade extends AbstractFacade<AdminData> {
  @PersistenceContext(unitName = "ssbd01mokPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AdminDataFacade() {
    super(AdminData.class);
  }
}
