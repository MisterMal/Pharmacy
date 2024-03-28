package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Table(name = "account")
@Setter
@NamedQuery(name = "account.findAll", query = "SELECT o FROM Account o")
@NamedQuery(name = "account.findByLogin", query = "SELECT o FROM Account o WHERE o.login = ?1")
@NamedQuery(name = "account.findByEmail", query = "SELECT o FROM Account o WHERE o.email = ?1")
public class Account extends AbstractEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @OneToMany(
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH},
      mappedBy = "account")
  @ToString.Exclude
  Set<AccessLevel> accessLevels = new HashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @Column(unique = true, nullable = false)
  @Size(max = 50, min = 5)
  @NotNull
  private String login;

  @Size(max = 50, min = 5)
  @Email
  @NotNull
  @Column(unique = true, nullable = false)
  @Setter
  private String email;

  @ToString.Exclude
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean active;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean confirmed;

  @Basic(optional = false)
  private Locale language;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_positive_login")
  private Date lastPositiveLogin;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_negative_login")
  private Date lastNegativeLogin;

  @Column(name = "logical_address")
  private String logicalAddress;

  @Column(name = "incorrect_login_attempts")
  private int incorrectLoginAttempts;

  @Builder
  public Account(String login, String password) {
    this.login = login;
    this.password = password;
  }
}
