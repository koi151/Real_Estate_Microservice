package com.example.msaccount.repository;

import com.example.msaccount.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    Optional<Account> findByAccountName(String accountName);
//    boolean existsByAccountIdAndDeleted(Long id, boolean deleted);
//    boolean existsByAccountName(String accountName);
//    boolean existsByPhone(String phone);
//    Optional<Account> findByAccountIdAndAccountStatusAndDeleted(Long id, AccountStatusEnum status, boolean deleted);
//    Page<Account> findByAccountStatus(AccountStatusEnum se, PageRequest pageRequest);
}
