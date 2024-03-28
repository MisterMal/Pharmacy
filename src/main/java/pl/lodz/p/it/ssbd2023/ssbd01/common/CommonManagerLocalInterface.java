package pl.lodz.p.it.ssbd2023.ssbd01.common;

import jakarta.ejb.Local;

@Local
public interface CommonManagerLocalInterface {

  boolean isLastTransactionRollback();
}
