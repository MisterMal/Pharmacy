package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.OrderFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.IN_QUEUE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({
        GenericFacadeExceptionsInterceptor.class,
        OrderFacadeExceptionsInterceptor.class,
        TrackerInterceptor.class
})
public class OrderFacade extends AbstractFacade<Order> {
    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderFacade() {
        super(Order.class);
    }

    @Override
    @PermitAll
    public List<Order> findAll() {
        return super.findAll();
    }

    @Override
    @RolesAllowed("createOrder")
    public void create(Order order) {
        super.create(order);
    }

    public List<Order> findAllByPatientId(Long id) {
        TypedQuery<Order> query = em.createNamedQuery("Order.findByPatientDataId", Order.class);
        query.setParameter("patientDataId", id);
        return query.getResultList();
    }

    @RolesAllowed("getWaitingOrders")
    public List<Order> findWaitingOrders() {
        return getEntityManager()
                .createQuery("select distinct o from Order o "
                        + "left join fetch o.orderMedications "
                        + "where o.orderState = pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.IN_QUEUE")
                .getResultList();
    }

    @RolesAllowed("updateQueue")
    public List<Order> findAllOrdersInQueueSortByOrderDate() {
        TypedQuery<Order> query = em.createNamedQuery("Order.findAllOrdersStateInQueueSortByOrderDate", Order.class);
        return query.getResultList();
    }

    @RolesAllowed("createOrder")
    public List<Order> findOrdersInQueueContainingMedicationsSortByOrderDate(List<Medication> medications) {
        return getEntityManager()
                .createQuery("SELECT o FROM Order o " +
                        "left join fetch o.orderMedications om " +
                        "WHERE o.orderState = :state " +
                        "AND o in (select o from Order o " +
                            "left join o.orderMedications om " +
                            "where om.medication in :medications)" +
                        "order by o.orderDate ASC")
                .setParameter("state", IN_QUEUE)
                .setParameter("medications", medications)
                .getResultList();
    }

    @RolesAllowed("getOrdersToApprove")
    public List<Order> findNotYetApproved() {
        return getEntityManager()
                .createQuery("select o from Order o "
                        + "left join fetch o.orderMedications "
                        + "where o.prescription is not null "
                        + "and o.orderState = pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.WAITING_FOR_CHEMIST_APPROVAL")
                .getResultList();
    }


    @RolesAllowed("deleteWaitingOrdersById")
    public void deleteWaitingOrdersById(Long id) {
        String orderQuery = "UPDATE Order o SET o.orderState = :newState "
                + "WHERE o.id = :orderId AND o.orderState = :currentState";

        getEntityManager()
                .createQuery(orderQuery)
                .setParameter("newState", OrderState.REJECTED_BY_CHEMIST)
                .setParameter("currentState", IN_QUEUE)
                .setParameter("orderId", id)
                .executeUpdate();

        }


  @RolesAllowed("withdraw")
  public void withdrawOrder(Long id, Long userId){
      String updateStateQuery = "UPDATE Order o " +
              "SET o.orderState = :newState " +
              "WHERE o.id = :orderId " +
              "AND o.orderState = :currentState " +
              "AND o.patientData.id = :userId";

      getEntityManager()
              .createQuery(updateStateQuery)
              .setParameter("newState", OrderState.REJECTED_BY_PATIENT)
              .setParameter("currentState", OrderState.TO_BE_APPROVED_BY_PATIENT)
              .setParameter("orderId", id)
              .setParameter("userId", userId)
              .executeUpdate();

  }

    @RolesAllowed("approvedByPatient")
    public void approvedByPatient(Long id, Long userId){
        String updateStateQuery = "UPDATE Order o " +
                "SET o.orderState = :newState " +
                "WHERE o.id = :orderId " +
                "AND o.orderState = :currentState " +
                "AND o.patientData.id = :userId";

        getEntityManager()
                .createQuery(updateStateQuery)
                .setParameter("newState", OrderState.APPROVED_BY_PATIENT)
                .setParameter("currentState", OrderState.TO_BE_APPROVED_BY_PATIENT)
                .setParameter("orderId", id)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @RolesAllowed("cancelOrder")
    public void cancelOrder(Long id, Long chemistId) {
        String updateStateQuery = "UPDATE Order o " +
                "SET o.orderState = :newState, o.chemistData.id = :chemistId, o.modificationDate = :mod " +
                "WHERE o.id = :orderId " +
                "AND o.orderState = :currentState ";
        getEntityManager()
                .createQuery(updateStateQuery)
                .setParameter("newState", OrderState.REJECTED_BY_CHEMIST)
                .setParameter("currentState", OrderState.WAITING_FOR_CHEMIST_APPROVAL)
                .setParameter("orderId", id)
                .setParameter("chemistId", chemistId)
                .setParameter("mod", Date.from(Instant.now()))
                .executeUpdate();
    }

    @Override
    @PermitAll
    public void edit(Order order) {
        super.edit(order);
    }

    @PermitAll
    public Optional<Order> find(Long id) {
        return super.find(id);
    }
}
