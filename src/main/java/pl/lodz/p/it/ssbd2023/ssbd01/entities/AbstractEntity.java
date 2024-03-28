package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

  @Version
  @Setter(lombok.AccessLevel.NONE)
  private Long version;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date", nullable = false, updatable = false)
  private Date creationDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modification_date")
  private Date modificationDate;

  @Column(name = "created_by")
  private String createdBy;

  @PrePersist
  public void prePersist() {
    this.creationDate = new Date();
  }

  @PreUpdate
  public void preUpdate() {
    this.modificationDate = new Date();
  }

  @Column(name = "modified_by")
  private String modifiedBy;

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.getId() != null ? this.getId().hashCode() : 0);
    return hash;
  }

  public abstract Long getId();

  @Override
  public boolean equals(Object object) {
    if (object.getClass() != this.getClass()) {
      return false;
    }
    AbstractEntity other = (AbstractEntity) object;
    return (this.getId() != null || other.getId() == null)
        && (this.getId() == null || this.getId().equals(other.getId()));
  }
}
