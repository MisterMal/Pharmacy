package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.List;

public interface OrderManagerLocal extends CommonManagerLocalInterface {

    void createOrder(Order order);

    Order getOrder(Long id);

    List<Order> getAllOrders();

    List<Order> getAllOrdersForSelf(Account account);

    List<Order> getWaitingOrders();

    List<Order> getOrdersToApprove();

    void approveOrder(Long id);

    void cancelOrder(Long id);

    void deleteWaitingOrderById(Long id);

    void withdrawOrder(Long id);

    void approvedByPatient(Long id);

    void updateQueue();
}
