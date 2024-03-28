package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.EtagVerification;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;

import java.util.List;

public interface ShipmentManagerLocal extends CommonManagerLocalInterface {

    void createShipment(Shipment shipment, EtagVerification etagVerification);

    Shipment getShipment(Long id);

    List<Shipment> getAllShipments();
}
