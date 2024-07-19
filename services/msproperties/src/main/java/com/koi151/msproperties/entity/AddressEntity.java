package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "city", nullable = false, length = 50)
    @NotBlank(message = "City name is mandatory")
    @Size(max = 50, message = "City name cannot exceed {max} characters length")
    private String city;

    @Column(name = "district", nullable = false, length = 50)
    @NotBlank(message = "District name is mandatory")
    @Size(max = 50, message = "District name cannot exceed {max} characters length")
    private String district;

    @Column(name = "ward", nullable = false, length = 50)
    @NotEmpty(message = "Ward name is mandatory")
    @Size(max = 50, message = "Ward name cannot exceed {max} characters length")
    private String ward;

    @Column(name = "street_address", length = 150)
    @Size(max = 150, message = "Street address cannot exceed {max} characters length")
    private String streetAddress;
}
