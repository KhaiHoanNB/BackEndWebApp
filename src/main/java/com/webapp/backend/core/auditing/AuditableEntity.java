package com.webapp.backend.core.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.Transient;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditListener.class)
public class AuditableEntity implements Serializable {

    @Serial
    @Transient
    private static final long serialVersionUID = 1L;

    @Column(
            name = "create_date",
            nullable = false
    )
    private LocalDateTime createDate;

    @Column(
            name = "created_by",
            length = 100,
            nullable = false
    )
    private String createdBy;

    @Column(
            name = "modify_date",
            nullable = true
    )
    private LocalDateTime modifyDate;

    @Column(
            name = "modified_by",
            length = 100,
            nullable = true
    )
    private String modifiedBy;

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
