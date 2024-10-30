package com.koi151.msproperty.entity;

import com.koi151.msproperty.enums.DirectionEnum;
import com.koi151.msproperty.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@NamedEntityGraph(name = "property-with-details",
    attributeNodes = {
//        @NamedAttributeNode("propertyPostService"),
        @NamedAttributeNode("propertyForRent"),
        @NamedAttributeNode("propertyForSale")
    })
@Table(
    indexes = {
        @Index(name = "idx_area", columnList = "area"),
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_category_id", columnList = "category_id"),
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_available_from", columnList = "available_from"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_house_direction", columnList = "house_direction"),
        @Index(name = "idx_balcony_direction", columnList = "balcony_direction")

        // Composite Indexes
//        @Index(name = "idx_title_area_category", columnList = "title, area, category_id"),

    }
)
@Entity(name = "property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 58116615802509522L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForSale propertyForSale;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForRent propertyForRent;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Address address;

//    @OneToOne(mappedBy = "property",  fetch = FetchType.LAZY,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    private PropertyPostService propertyPostService;

    @OneToMany(mappedBy = "property",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Rooms> rooms;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category id is mandatory")
    private int categoryId;

    @Column(name = "account_id", nullable = false)
    @NotNull(message = "Account id is mandatory")
    private String accountId;

    @Column(name = "available_from", nullable = false, columnDefinition = "DATE")
    @NotNull(message = "Property available date is mandatory")
    private LocalDate availableFrom;

    @Column(name = "title", nullable = false, length = 100)
    @NotBlank(message = "Property post title is mandatory")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Column(name = "area", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private BigDecimal area;

    @Column(name = "description", columnDefinition = "TEXT", length = 5000)
    @Size(max = 5000, message = "Property description cannot exceed 5000 characters")
    private String description;

    @Column(name = "images", columnDefinition = "TEXT")
    private String imageUrls;

    @Column(name = "view", columnDefinition = "INT UNSIGNED", nullable = false)
    private int view = 0;

    @Column(name = "total_floor", columnDefinition = "SMALLINT UNSIGNED")
    @PositiveOrZero(message = "Total floor must be non-negative value")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    private short totalFloor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_direction", length = 20)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    @Column(name = "balcony_direction", length = 20)
    private DirectionEnum balconyDirection;
}