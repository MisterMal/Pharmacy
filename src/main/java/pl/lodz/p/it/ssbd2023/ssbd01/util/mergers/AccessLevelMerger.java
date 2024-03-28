package pl.lodz.p.it.ssbd2023.ssbd01.util.mergers;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

public class AccessLevelMerger {

  public static void mergeAccessLevels(AccessLevel managed, AccessLevel toMerge) {
    if (managed instanceof PatientData) {
      mergePatientData((PatientData) managed, (PatientData) toMerge);
    }
    if (managed instanceof ChemistData) {
      mergeChemistData((ChemistData) managed, (ChemistData) toMerge);
    }
    if (managed instanceof AdminData) {
      mergeAdminData((AdminData) managed, (AdminData) toMerge);
    }
  }

  public static void mergePatientData(PatientData managed, PatientData toMerge) {
    managed.setPesel(toMerge.getPesel());
    managed.setFirstName(toMerge.getFirstName());
    managed.setLastName(toMerge.getLastName());
    managed.setPhoneNumber(toMerge.getPhoneNumber());
    managed.setNIP(toMerge.getNIP());
  }

  public static void mergeChemistData(ChemistData managed, ChemistData toMerge) {
    managed.setLicenseNumber(toMerge.getLicenseNumber());
  }

  public static void mergeAdminData(AdminData managed, AdminData toMerge) {
    managed.setWorkPhoneNumber(toMerge.getWorkPhoneNumber());
  }
}
