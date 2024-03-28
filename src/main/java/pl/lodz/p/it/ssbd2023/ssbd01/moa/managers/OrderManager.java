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
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationExceptionEntityNotFound;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.OrderException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.*;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@Stateful
@DenyAll
public class OrderManager extends AbstractManager
        implements OrderManagerLocal, SessionSynchronization {

    @Inject
    private OrderFacade orderFacade;
    @Inject
    private MedicationFacade medicationFacade;
    @Inject
    private ShipmentFacade shipmentFacade;
    @Inject
    private AccountFacade accountFacade;
    @Context
    private SecurityContext context;


    @Override
    @RolesAllowed("createOrder")
    public void createOrder(Order order) {
        Account account = getCurrentUserWithAccessLevels();
        AccessLevel patientData = AccessLevelFinder.findAccessLevel(account, Role.PATIENT);
        order.setPatientData(patientData);

        // load full medication object
        order.getOrderMedications().forEach(om -> {
            om.setOrder(order);
            Medication medication = medicationFacade.findByName(
                    om.getMedication().getName());
            om.setMedication(medication);
            om.setPurchasePrice(medication.getCurrentPrice());
        });

        // assert that prescription is provided if needed
        if(checkIsOnPrescription(order)) {
            if(order.getPrescription() == null) {
                throw OrderException.createPrescriptionRequired();
            }
            order.getPrescription().setPatientData(patientData);
            order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);
        }

        List<Shipment> shipmentsNotProcessed = shipmentFacade.findAllNotAlreadyProcessed();
        if(shipmentsNotProcessed.isEmpty()) {
            processOrderMedicationsStock(order);
        } else {
            partiallyCalculateQueue(order, shipmentsNotProcessed);
        }
        orderFacade.create(order);
    }

    @RolesAllowed("createOrder")
    private boolean checkIsOnPrescription(Order order) {
        for (OrderMedication om : order.getOrderMedications()) {
            if (om.getMedication().getCategory().getIsOnPrescription()) {
                return true;
            }
        }
        return false;
    }

    @RolesAllowed("createOrder")
    private void partiallyCalculateQueue(Order order, List<Shipment> shipmentsNotProcessed) {

        // get list of all medications in current order
        List<Medication> medicationsInOrder = order.getOrderMedications().stream()
                .map(OrderMedication::getMedication)
                .collect(Collectors.toList());

        // get list of all orders containing one or more of given medications
        List<Order> ordersInQueueToProcess =
                orderFacade.findOrdersInQueueContainingMedicationsSortByOrderDate(medicationsInOrder);
        Set<Medication> medicationsToProcess = ordersInQueueToProcess.stream()
                .flatMap(o -> o.getOrderMedications().stream()
                        .map(OrderMedication::getMedication))
                .collect(Collectors.toSet());

        // calculate shipments only for medications in found orders
        increaseMedicationStock(shipmentsNotProcessed, medicationsToProcess);

        // process orders from queue
        ordersInQueueToProcess.forEach(this::processOrderMedicationsStock);

        // process order that is being created
        processOrderMedicationsStock(order);
    }

    @RolesAllowed("createOrder")
    private void increaseMedicationStock(List<Shipment> shipmentsNotProcessed,
                                         Set<Medication> medicationsToProcess) {
        shipmentsNotProcessed.forEach(shipment -> {
            shipment.getShipmentMedications().forEach(sm -> {
                if(medicationsToProcess.contains(sm.getMedication())) {
                    sm.getMedication().setStock(
                            sm.getMedication().getStock() + sm.getQuantity());
                    sm.setProcessed(true);
                }
            });
        });
    }

    @RolesAllowed({"updateQueue", "createOrder"})
    private void processOrderMedicationsStock(Order order) {
        // check if stock can be decreased
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            // check if stock is sufficient
            if((orderMedication.getMedication().getStock() - orderMedication.getQuantity()) < 0) {
                order.setOrderState(OrderState.IN_QUEUE);
                return;
            }
            // check if medication price has had changed
            if (orderMedication.getPurchasePrice() != null &&
                    (medication.getCurrentPrice().compareTo(orderMedication.getPurchasePrice()) > 0)) {
                order.setOrderState(OrderState.TO_BE_APPROVED_BY_PATIENT);
                return;
            }
        }
        // decrease stock
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            medication.setStock(medication.getStock() - orderMedication.getQuantity());
        }
        // medications on prescription need to be additionally accepted by chemist
        order.setOrderState(OrderState.FINALISED);
        if (order.getPrescription() != null) {
            order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);
        }
    }

    @Override
    @RolesAllowed("updateQueue")
    public void updateQueue() {
        List<Order> ordersInQueue = orderFacade.findAllOrdersInQueueSortByOrderDate();
        List<Shipment> shipmentsNotProcessed = shipmentFacade.findAllNotAlreadyProcessed();

        shipmentsNotProcessed.forEach(shipment ->
            shipment.getShipmentMedications().forEach(shipmentMedication -> {
                shipmentMedication.getMedication()
                        .setStock(shipmentMedication.getMedication().getStock()
                                + shipmentMedication.getQuantity());
                shipmentMedication.setProcessed(true);
            })
        );
        ordersInQueue.forEach(this::processOrderMedicationsStock);
    }

    @Override
    @DenyAll
    public Order getOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Order> getAllOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("getAllOrdersForSelf")
    public List<Order> getAllOrdersForSelf(Account account) {
        try {
            AccessLevel patientData = AccessLevelFinder.findAccessLevel(account, Role.PATIENT);
            return orderFacade.findAllByPatientId(patientData.getId());
        } catch(ApplicationExceptionEntityNotFound e) {
            throw OrderException.onlyPatientCanListOrders();
        }
    }

    @Override
    @RolesAllowed("getWaitingOrders")
    public List<Order> getWaitingOrders() {
        return orderFacade.findWaitingOrders();
    }

    @Override
    @RolesAllowed("getOrdersToApprove")
    public List<Order> getOrdersToApprove() {
        return orderFacade.findNotYetApproved();
    }

    @Override
    @RolesAllowed("approveOrder")
    public void approveOrder(Long id) {
        Order order = orderFacade.find(id).orElseThrow();
        if (!order.getOrderState().equals(OrderState.WAITING_FOR_CHEMIST_APPROVAL)) {
            throw OrderException.createModificationOrderOfIllegalState();
        }
        order.setOrderState(OrderState.FINALISED);
        orderFacade.edit(order);
    }

    @Override
    @RolesAllowed("cancelOrder")
    public void cancelOrder(Long id) {
        Account account = getCurrentUser();
        Order order = orderFacade.find(id).orElseThrow();
        if (!order.getOrderState().equals(OrderState.WAITING_FOR_CHEMIST_APPROVAL)) {
            throw OrderException.createModificationOrderOfIllegalState();
        }
        orderFacade.cancelOrder(id, account.getId());

    }

    @Override
    @RolesAllowed("withdraw")
    public void withdrawOrder(Long id) {
        Account account = getCurrentUser();
        Order order = orderFacade.find(id).orElseThrow();
        if (!order.getOrderState().equals(OrderState.TO_BE_APPROVED_BY_PATIENT)) {
            throw OrderException.noPermissionToDeleteOrder();
        }
        orderFacade.withdrawOrder(id, account.getId());
        order.setOrderState(OrderState.REJECTED_BY_PATIENT);
    }

    @Override
    @RolesAllowed("approvedByPatient")
    public void approvedByPatient(Long id) {
        Account account = getCurrentUser();
        Order order = orderFacade.find(id)
                .orElseThrow(() -> OrderException.orderNotFound(id));

        if (order.getOrderState() != OrderState.TO_BE_APPROVED_BY_PATIENT) {
            throw OrderException.noPermissionToApproveOrder();
        }

        orderFacade.approvedByPatient(id, account.getId());
        decreaseMedicationStock(order);

        if (order.getPrescription() != null) {
            order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);
        } else {
            order.setOrderState(OrderState.FINALISED);
        }
    }

    @RolesAllowed("approvedByPatient")
    private void decreaseMedicationStock(Order order) {
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            int requestedQuantity = orderMedication.getQuantity();

            int currentStock = medication.getStock();
            int updatedStock = currentStock - requestedQuantity;
            medication.setStock(updatedStock);
            medicationFacade.edit(medication);
        }
    }

    @Override
    @RolesAllowed("deleteWaitingOrdersById")
    public void deleteWaitingOrderById(Long id) {
        Optional<Order> order = orderFacade.find(id);
        if (order.get().getOrderState() != OrderState.IN_QUEUE) {
            throw OrderException.orderNotInQueue();
        }
        orderFacade.deleteWaitingOrdersById(id);
    }


    @RolesAllowed("getCurrentUser")
    public Account getCurrentUser() {
        return accountFacade.findByLogin(getCurrentUserLogin());
    }

    @RolesAllowed("getCurrentUserWithAccessLevels")
    public Account getCurrentUserWithAccessLevels() {

        return accountFacade.findByLoginAndRefresh(getCurrentUserLogin());
    }

    @PermitAll
    public String getCurrentUserLogin() {
        return context.getUserPrincipal().getName();
    }
}
