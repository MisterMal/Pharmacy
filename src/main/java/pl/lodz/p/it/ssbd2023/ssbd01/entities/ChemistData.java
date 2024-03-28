package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Table(name = "chemist_data")
@Setter
@DiscriminatorValue("CHEMIST")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@NamedQuery(name = "chemistData.findAll", query = "SELECT o FROM ChemistData o")
public class ChemistData extends AccessLevel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Builder
  public ChemistData(Long id, String licenseNumber) {
    super(id, Role.CHEMIST);
    this.licenseNumber = licenseNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{6}$", message = "Invalid license number")
  @Column(nullable = false, unique = true, name = "license_number")
  private String licenseNumber;
}
