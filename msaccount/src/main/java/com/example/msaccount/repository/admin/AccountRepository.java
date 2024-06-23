package com.example.msaccount.repository.admin;

import com.example.msaccount.entity.AccountEntity;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByPhone(String phone);
    Optional<AccountEntity> findByAccountIdAndDeleted(Integer id, boolean deleted);
    Page<AccountEntity> findByAccountStatusAndDeleted(AccountStatusEnum se, boolean deleted, PageRequest pageRequest);
}
