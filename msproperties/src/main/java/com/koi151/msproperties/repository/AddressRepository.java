package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
