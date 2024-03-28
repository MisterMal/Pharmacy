package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import lombok.Getter;

@Getter
public enum Role {
  ADMIN("ADMIN", "Administrator"),
  PATIENT("PATIENT", "Pacjent"),
  CHEMIST("CHEMIST", "Farmaceuta");

  private String roleName;
  private String roleDescription;

  Role(String roleName, String roleDescription) {
    this.roleName = roleName;
    this.roleDescription = roleDescription;
  }
}
