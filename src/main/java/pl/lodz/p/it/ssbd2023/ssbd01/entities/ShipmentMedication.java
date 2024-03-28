package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.Media;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "shipment_medication",
    indexes = {
      @Index(name = "shipment_m_index", columnList = "shipment_id"),
      @Index(name = "medication_index", columnList = "medication_id")
    })
@NamedQuery(name = "shipmentMedication.findAll", query = "SELECT o FROM ShipmentMedication o")
public class ShipmentMedication extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Builder
  public ShipmentMedication(Medication medication, Integer quantity) {
    this.medication = medication;
    this.quantity = quantity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "shipment_id", updatable = false, nullable = false)
  private Shipment shipment;

  @Column(nullable = false, name = "processed")
  @NotNull
  private Boolean processed = false;

  @ManyToOne(optional = false, cascade = {CascadeType.REFRESH})
  @JoinColumn(name = "medication_id", referencedColumnName = "id",
          updatable = false, nullable = false)
  private Medication medication;

  @Column(nullable = false)
  @Min(value = 1, message = "Quantity must be greater than 0")
  private Integer quantity;
}
