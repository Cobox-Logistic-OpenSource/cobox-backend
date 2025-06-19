package coboxlogistic.startup.backend.shared.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all aggregate roots in the Cobox Backend Platform.
 * Provides auditing capabilities and domain event management following DDD principles.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableAbstractAggregateRoot<ID> {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * Get the aggregate ID
     * @return the aggregate identifier
     */
    public abstract ID getId();

    /**
     * Add a domain event to the aggregate
     * @param event the domain event to add
     */
    protected void addDomainEvent(Object event) {
        this.domainEvents.add(event);
    }

    /**
     * Get all domain events and clear the list
     * @return list of domain events
     */
    public List<Object> getDomainEvents() {
        List<Object> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    /**
     * Check if the aggregate has domain events
     * @return true if there are domain events
     */
    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    /**
     * Clear all domain events
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 