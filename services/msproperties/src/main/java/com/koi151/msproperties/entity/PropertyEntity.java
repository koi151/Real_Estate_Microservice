package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 58116615802509522L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @OneToOne(mappedBy = "propertyEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForSaleEntity propertyForSale;

    @OneToOne(mappedBy = "propertyEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForRentEntity propertyForRent;

    @OneToOne(mappedBy = "propertyEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @OneToOne(mappedBy = "propertyEntity",  fetch = FetchType.LAZY, // querying for properties without needing their payment details.
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyPostServiceEntity propertyPostService;

    @OneToMany(mappedBy = "propertyEntity", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<RoomEntity> rooms;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category id is mandatory")
    @Positive(message = "Category id must be positive value")
    private int categoryId;

    @Column(name = "account_id", nullable = false)
    @NotNull(message = "Account id is mandatory")
    @Positive(message = "Account id must be positive value")
    private long accountId;

    @Column(name = "available_from", nullable = false, length = 30)
    @NotEmpty(message = "Available time cannot be empty")
    @Size(max = 30, message = "Available time cannot exceed 30 characters")
    private String availableFrom;

    @Column(name = "title", nullable = false, length = 100)
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Column(name = "area", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private BigDecimal area;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "images", columnDefinition = "TEXT")
    private String imageUrls;

    @Column(name = "view", nullable = false)
    @NotNull(message = "Number of views are mandatory")
    @PositiveOrZero(message = "View number must be non-negative value")
    private int view;

    @Column(name = "total_floor", columnDefinition = "SMALLINT UNSIGNED")
    @PositiveOrZero(message = "Total floor must be non-negative value")
    private short totalFloor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_direction", length = 20)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    @Column(name = "balcony_direction", length = 20)
    private DirectionEnum balconyDirection;
}