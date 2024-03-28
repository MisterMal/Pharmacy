package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "patient_order",
        indexes = {
                @Index(name = "order_index", columnList = "id"),
                @Index(name = "prescription_index", columnList = "prescription_id", unique = true),
                @Index(name = "patient_data_index", columnList = "patient_data_id"),
                @Index(name = "chemist_data_index", columnList = "chemist_data_id"),
        })
@NamedQuery(
        name = "Order.findByPatientDataId",
        query =
                "SELECT o FROM Order o JOIN FETCH o.orderMedications WHERE o.patientData.id = :patientDataId")
@NamedQuery(
        name = "Order.findAllOrdersStateInQueueSortByOrderDate",
        query =
                "SELECT o FROM Order o WHERE o.orderState = pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.IN_QUEUE ORDER BY o.orderDate ASC")
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "order_date")
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private OrderState orderState;

    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            orphanRemoval = true)
    private List<OrderMedication> orderMedications = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "prescription_id", referencedColumnName = "id", updatable = false)
    private Prescription prescription;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "patient_data_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false)
    private AccessLevel patientData;

    @ManyToOne
    @JoinColumn(name = "chemist_data_id", referencedColumnName = "id", updatable = false)
    private AccessLevel chemistData;

    @Builder
    public Order(
            Date orderDate,
            AccessLevel patientData,
            AccessLevel chemistData) {
        this.orderDate = orderDate;
        this.patientData = patientData;
        this.chemistData = chemistData;
    }

    @Builder(builderMethodName = "createBuilder", buildMethodName = "createBuild")
    public Order(Date orderDate, Prescription prescription,
                 List<OrderMedication> orderMedications) {
        this.orderDate = orderDate;
        this.prescription = prescription;
        this.orderMedications = new ArrayList<>(orderMedications);
    }
}
