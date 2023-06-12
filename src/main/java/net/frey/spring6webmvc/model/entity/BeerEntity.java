package net.frey.spring6webmvc.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.frey.spring6webmvc.model.BeerStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerEntity {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private int version;

    @NotBlank
    @Column(length = 50)
    @Size(max = 50)
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "beer")
    private Set<BeerOrderLine> lineItems;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Builder.Default
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "beer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getBeers().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getBeers().remove(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeerEntity that)) return false;

        if (getVersion() != that.getVersion()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getBeerName() != null ? !getBeerName().equals(that.getBeerName()) : that.getBeerName() != null)
            return false;
        if (getBeerStyle() != that.getBeerStyle()) return false;
        if (getUpc() != null ? !getUpc().equals(that.getUpc()) : that.getUpc() != null) return false;
        if (getQuantityOnHand() != null
                ? !getQuantityOnHand().equals(that.getQuantityOnHand())
                : that.getQuantityOnHand() != null) return false;
        if (getPrice() != null ? !getPrice().equals(that.getPrice()) : that.getPrice() != null) return false;
        if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate()) : that.getCreatedDate() != null)
            return false;
        return getUpdatedDate() != null
                ? getUpdatedDate().equals(that.getUpdatedDate())
                : that.getUpdatedDate() == null;
    }
}
