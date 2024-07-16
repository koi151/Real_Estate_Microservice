package com.example.msaccount.repository;

import com.example.msaccount.entity.Account;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);
    boolean existsByAccountIdAndDeleted(Long id, boolean deleted);
    boolean existsByAccountName(String accountName);
    boolean existsByPhone(String phone);
    Optional<Account> findByAccountIdAndDeleted(Long id, boolean deleted);
    Page<Account> findByAccountStatus(AccountStatusEnum se, PageRequest pageRequest);
}
