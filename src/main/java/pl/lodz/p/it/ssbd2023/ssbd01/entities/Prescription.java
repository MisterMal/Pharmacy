package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "prescription",
    indexes = {
      @Index(
          name = "patient_data_index_perscription",
          columnList = "patient_data_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"patient_data_id", "prescription_number"}),
    })
public class Prescription extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @ManyToOne(
      optional = false,
      cascade = {CascadeType.REFRESH})
  @JoinColumn(name = "patient_data_id", nullable = false, updatable = false)
  private AccessLevel patientData;

  @Column(nullable = false, name = "prescription_number")
  private String prescriptionNumber;

  @Builder
  public Prescription(String prescriptionNumber) {
    this.prescriptionNumber = prescriptionNumber;
  }
}
