package com.koi151.msproperty.entity;

import com.koi151.msproperty.enums.DirectionEnum;
import com.koi151.msproperty.enums.FurnitureEnum;
import com.koi151.msproperty.enums.LegalDocumentEnum;
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
        @NamedAttributeNode("propertyForRent"),
        @NamedAttributeNode("propertyForSale"),
        @NamedAttributeNode("address"),
        @NamedAttributeNode("rooms")
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
        @Index(name = "idx_balcony_direction", columnList = "balcony_direction"),
        @Index(name = "idx_legal_document", columnList = "legal_document"),
        @Index(name = "idx_furnitures", columnList = "furnitures")
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

    @OneToMany(mappedBy = "property",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Rooms> rooms;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category id is mandatory")
    private int categoryId;

    @Column(name = "account_id", nullable = false)
    @NotNull(message = "Account id is mandatory")
    private String accountId;

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

    @Column(name = "view", columnDefinition = "INT UNSIGNED")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "legal_document")
    private LegalDocumentEnum legalDocument;

    @Enumerated(EnumType.STRING)
    @Column(name = "furnitures", length = 20)
    private FurnitureEnum furniture;

//    @Column(name = "contact_name", length = 100)
//    @NotBlank(message = "Contact name is mandatory")
//    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Contact name must contain only letters and spaces")
//    private String contactName;
//
//    @Column(name = "contact_phone", length = 100)
//    @NotBlank(message = "Phone number is mandatory")
//    private String contactPhone;
//
//    @Column(name = "contact_email", length = 100)
//    @Email(message = "Invalid email")
//    private String contactEmail;
}