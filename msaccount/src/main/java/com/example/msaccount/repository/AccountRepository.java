package com.example.msaccount.repository;

import com.example.msaccount.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByPhone(String phone);
}
