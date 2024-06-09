package com.example.msaccount.repository;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.AccountStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByPhone(String phone);
    Optional<Account> findByAccountIdAndDeleted(Integer id, boolean deleted);
    Page<Account> findByAccountStatusAndDeleted(AccountStatusEnum se, boolean deleted, PageRequest pageRequest);
}
