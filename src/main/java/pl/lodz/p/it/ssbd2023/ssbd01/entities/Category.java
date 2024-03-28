package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "category")
@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "category.findAll", query = "SELECT o FROM Category o")
@NamedQuery(name = "category.findByName", query = "SELECT o FROM Category o WHERE o.name = ?1")
public class Category extends AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @NotNull
  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  @NotNull
  private Boolean isOnPrescription;

  @Builder
  public Category(String name, Boolean isOnPrescription) {
    this.name = name;
    this.isOnPrescription = isOnPrescription;
  }
}
