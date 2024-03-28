package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtagVerification {
  private Map<String, EtagVersion> etagVersionList;
}
