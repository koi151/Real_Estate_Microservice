package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    private int id;

    @Column(name = "city", nullable = false, length = 50)
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @Column(name = "district", nullable = false, length = 50)
    @NotEmpty(message = "District cannot be empty")
    private String district;

    @Column(name = "ward", nullable = false, length = 50)
    @NotEmpty(message = "Ward cannot be empty")
    private String ward;

    @Column(name = "street_address", length = 150)
    private String streetAddress;

    public AddressEntity(String city, String district, String ward, String streetAddress) {
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.streetAddress = streetAddress;
    }
}
