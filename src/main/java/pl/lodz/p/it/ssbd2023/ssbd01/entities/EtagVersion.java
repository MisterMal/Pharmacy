package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtagVersion {
  private Long version;
  private String etag;
}
