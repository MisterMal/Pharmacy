package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Table(name = "admin_data")
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@ToString(callSuper = true)
@NamedQuery(name = "adminData.findAll", query = "SELECT o FROM AdminData o")
public class AdminData extends AccessLevel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, name = "work_phone_number")
  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;

  @Builder
  public AdminData(Long id, String workPhoneNumber) {
    super(id, Role.ADMIN);
    this.workPhoneNumber = workPhoneNumber;
  }
}
