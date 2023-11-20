package com.webapp.backend.core.auditing;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class AuditListener {

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static Object getPrincipal() {
        if (!isAuthenticated()) {
            return null;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getPrincipal();
        }
    }

    @PrePersist
    public void beforePersist(AuditableEntity auditableEntity) {
        LocalDateTime now = LocalDateTime.now();

        auditableEntity.setCreateDate(now);
        auditableEntity.setModifyDate(now);

        Object principal = getPrincipal();
        if (principal != null) {
            if (principal instanceof UserDetails user) {
                auditableEntity.setCreatedBy(user.getUsername());
                auditableEntity.setModifiedBy(user.getUsername());
            } else {
                auditableEntity.setCreatedBy(principal.toString());
                auditableEntity.setModifiedBy(principal.toString());
            }
        } else {
            auditableEntity.setCreatedBy("admin");
        }
    }

    @PreUpdate
    public void beforeMerge(AuditableEntity auditableEntity) {
        LocalDateTime now = LocalDateTime.now();

        auditableEntity.setModifyDate(now);

        Object principal = getPrincipal();

        if (principal != null) {
            if (principal instanceof UserDetails user) {
                auditableEntity.setCreatedBy(user.getUsername());
                auditableEntity.setModifiedBy(user.getUsername());
            } else {
                auditableEntity.setCreatedBy(principal.toString());
                auditableEntity.setModifiedBy(principal.toString());
            }
        }

    }
}
