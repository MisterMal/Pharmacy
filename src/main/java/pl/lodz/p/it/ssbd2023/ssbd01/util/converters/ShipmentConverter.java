package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.MedicationCreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.EtagVerification;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.EtagVersion;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ShipmentConverter {
    private ShipmentConverter() {}

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    public static List<ShipmentDTO> mapShipmentsToShipmentsDto(List<Shipment> shipments) {
        return shipments == null ?
                null :
                shipments.stream()
                        .filter(Objects::nonNull)
                        .map(ShipmentConverter::mapShipmentToShipmentDto)
                        .collect(Collectors.toList());
    }

    public static ShipmentDTO mapShipmentToShipmentDto(Shipment shipment) {
        return ShipmentDTO.builder()
                .id(shipment.getId())
                .version(shipment.getVersion())
                .shipmentDate(shipment.getShipmentDate().toString())
                .shipmentMedications(
                        ShipmentMedicationConverter.mapShipmentMedsToShipmentMedsDto(
                                shipment.getShipmentMedications()))
                .build();
    }



    public static Shipment mapCreateShipmentDtoToShipment(CreateShipmentDTO shipment) {
        try {
            return Shipment.createBuilder()
                    .shipmentDate(Date.from(LocalDateTime.parse(shipment.getShipmentDate()).toInstant(ZoneOffset.UTC)))
                    .shipmentMedications(ShipmentMedicationConverter
                            .mapCreateShipmentMedsDtoToShipmentMeds(shipment.getShipmentMedications()))
                    .build();
        } catch(DateTimeParseException e) {
            throw ApplicationException.createIncorrectDateFormatException();
        }
    }

    public static EtagVerification mapCreateShipmentDtoToEtagVerification(CreateShipmentDTO shipment) {
        EtagVerification etagVerification = new EtagVerification(new HashMap<>());
        shipment.getShipmentMedications().forEach(sm -> {
            MedicationCreateShipmentDTO m = sm.getMedication();
            etagVerification.getEtagVersionList().put(m.getName(), EtagVersion.builder()
                    .version(m.getVersion())
                    .etag(m.getEtag())
                    .build());
        });
        return etagVerification;
    }

}
