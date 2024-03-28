package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "order_medication",
    indexes = {
      @Index(name = "order_m_index", columnList = "order_id"),
      @Index(name = "medication_index_om", columnList = "medication_id")
    })
@NamedQuery(name = "orderMedication.findAll", query = "SELECT o FROM OrderMedication o")
public class OrderMedication extends AbstractEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @Column(name = "purchase_price")
  @Digits(integer = 10, fraction = 2)
  @Min(value = 0, message = "Purchase price must be greater than or equal 0")
  private BigDecimal purchasePrice;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id", updatable = false, nullable = false)
  private Order order;

  @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
  @JoinColumn(name = "medication_id", updatable = false, nullable = false)
  private Medication medication;

  @Column(nullable = false)
  @Min(value = 1, message = "Quantity must be greater than 0")
  private Integer quantity;

  @Builder
  public OrderMedication(Order order, Medication medication, Integer quantity, BigDecimal purchasePrice) {
    this.setOrder(order);
    this.setMedication(medication);
    this.setQuantity(quantity);
    this.setPurchasePrice(purchasePrice);
  }
}
