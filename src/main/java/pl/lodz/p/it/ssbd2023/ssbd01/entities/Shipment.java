package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.*;
import lombok.AccessLevel;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(
    name = "shipment",
    indexes = {
      @Index(name = "shipment_index", columnList = "id", unique = true),
    })
@NamedQuery(
        name = "Shipment.findAllNotProcessed",
        query = "SELECT s FROM Shipment s " +
                "left join ShipmentMedication sm " +
                "where sm.processed = false")
public class Shipment extends AbstractEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @Builder(builderMethodName = "createBuilder")
  public Shipment(Date shipmentDate,
                  List<ShipmentMedication> shipmentMedications) {
    this.shipmentDate = shipmentDate;
    this.shipmentMedications = new ArrayList<>(shipmentMedications);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, name = "shipment_date", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date shipmentDate;

  @OneToMany(
      mappedBy = "shipment",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH},
      orphanRemoval = true)
  private List<ShipmentMedication> shipmentMedications = new ArrayList<>();
}
