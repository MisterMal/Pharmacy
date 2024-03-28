package pl.lodz.p.it.ssbd2023.ssbd01.util.comparators;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

import java.util.Comparator;

public class MedicationComparator {
    public static boolean equalsOmitStock(Medication a, Medication b) {
        return a.getCategory().getId().equals(b.getCategory().getId())
                && a.getName().equals(b.getName())
                && a.getCurrentPrice().compareTo(b.getCurrentPrice()) == 0;
    }
}
