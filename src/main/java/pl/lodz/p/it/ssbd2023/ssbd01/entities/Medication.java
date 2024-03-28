package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
    name = "medication",
    indexes = {@Index(name = "category_index", columnList = "category_id", unique = false)})
@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "medication.findAll", query = "SELECT o FROM Medication o")
@NamedQuery(name = "medication.findByName", query = "SELECT o FROM Medication o WHERE o.name = ?1")
public class Medication extends AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, unique = true, name = "medication_name")
  private String name;

  @Column(nullable = false)
  @Min(value = 0, message = "Stock must be greater than or equal to 0")
  private Integer stock;

  @Column(nullable = false, name = "current_price")
  @Digits(integer = 10, fraction = 2)
  @DecimalMin(value = "0.01", message = "Price must be greater than or equal 0")
  private BigDecimal currentPrice;

  @ManyToOne(
      optional = false,
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Builder
  public Medication(String name, Integer stock, BigDecimal currentPrice, Category category) {
    this.name = name;
    this.stock = stock;
    this.currentPrice = currentPrice;
    this.category = category;
  }

  @Builder(builderMethodName = "createShipmentBuilder")
  public Medication(String name, BigDecimal currentPrice) {
    this.name = name;
    this.currentPrice = currentPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Medication that = (Medication) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
