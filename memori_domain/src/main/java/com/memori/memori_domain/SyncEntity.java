package com.memori.memori_domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = {
    @Index(name = "idx_created_at", columnList = "created_at", unique = false),
    @Index(name = "idx_user_id", columnList = "user_id", unique = false),
    @Index(name = "idx_sort_order", columnList = "sort_order", unique = false)
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
public abstract class SyncEntity {

    @Id
    private UUID id;

    /*
     * CreatedAt (Timestamp): Captures when the row was created.
     * This can be useful for syncing operations, especially if you want to retrieve
     * all records created after a certain point in time.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /*
     * lastModified (Timestamp): Records the last time the row was updated.
     * This is crucial for resolving conflicts and determining the most recent
     * version of a row.
     */
    @Column(nullable = false)
    private Instant lastModified;

    /*
     * version (Integer or Long): A version number for each row that increments on
     * every update.
     * This can be used for optimistic locking and conflict resolution, as it allows
     * you to detect when a row has been updated by another device since it was last
     * fetched.
     */
    @Version
    @Column(nullable = false)
    private Long version;

    /*
     * deleted (Boolean or Timestamp): Indicates whether the row has been marked as
     * deleted.
     * Using a boolean is straightforward, but using a timestamp (e.g., deletedAt)
     * can provide additional context, such as when the row was deleted, which can
     * be helpful for sync operations that need to propagate deletions.
     */
    @Column(nullable = true)
    private Instant deletedAt;

    /*
     * syncedAt (Timestamp): Optionally, you can include a timestamp indicating when
     * the row was last successfully synced with the server or central database.
     * This can help in determining which rows need to be synced in the next
     * operation.
     */
    @Column(nullable = false)
    private Instant syncedAt;

    @Column(nullable = false)
    private UUID modifiedByDeviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "entity_type", insertable = false, updatable = false)
    private String entityType;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public abstract Integer getSortOrderConstant();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SyncEntity))
            return false;

        SyncEntity that = (SyncEntity) o;

        if (!id.equals(that.id))
            return false;
        if (!createdAt.equals(that.createdAt))
            return false;
        if (!lastModified.equals(that.lastModified))
            return false;
        if (!version.equals(that.version))
            return false;
        if (deletedAt != null && that.deletedAt != null) {
            if (!deletedAt.equals(that.deletedAt)) {
                return false;
            }
        } else if (!(deletedAt == null && that.deletedAt == null)) {
            return false;
        }
        if (!syncedAt.equals(that.syncedAt))
            return false;
        if (!modifiedByDeviceId.equals(that.modifiedByDeviceId))
            return false;
        if (!userId.equals(that.userId))
            return false;
        if (!sortOrder.equals(that.sortOrder))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }

    @Transient
    public String getEntityType() {
        DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        if (discriminatorValue == null || discriminatorValue.value() == null) {
            return this.getClass().getSimpleName();
        }
        return discriminatorValue.value();
    }
}