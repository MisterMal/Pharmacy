package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import lombok.*;

@Entity
@Table(name = "access_level", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"access_level_role", "account_id"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorColumn(name = "access_level_role", discriminatorType = DiscriminatorType.STRING)
public abstract class AccessLevel extends AbstractEntity implements Serializable {

  public static final Long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @Getter
  @Enumerated(EnumType.STRING)
  @Setter(lombok.AccessLevel.NONE)
  @Column(name = "access_level_role", insertable = false, nullable = false, updatable = false)
  private Role role;

  @Column(nullable = false, columnDefinition = "boolean default true")
  @NotNull
  private Boolean active = true;

  @ManyToOne(optional = false)
  @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
  private Account account;

  public AccessLevel(Long id, Role role) {
    this.id = id;
    this.role = role;
  }
}
