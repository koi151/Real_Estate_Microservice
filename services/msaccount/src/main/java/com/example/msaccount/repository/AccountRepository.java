package com.example.msaccount.repository;

import com.example.msaccount.entity.Account;
import com.example.msaccount.model.dto.ClientDatabaseAccountDTO;
import com.example.msaccount.model.dto.admin.AdminDatabaseAccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    Optional<Account> findByAccountName(String accountName);
//    boolean existsByAccountIdAndDeleted(Long id, boolean deleted);
//    boolean existsByAccountName(String accountName);
//    boolean existsByPhone(String phone);
    Optional<Account> findByAccountIdAndAccountEnableAndDeleted(String uuid, boolean isEnable, boolean deleted);

    @Query("SELECT new com.example.msaccount.model.dto.ClientDatabaseAccountDTO(acc.phone, acc.avatarUrl, ca.balance) " +
        "FROM account acc " +
        "JOIN client_account ca ON acc.accountId = ca.account.accountId " +
        "WHERE acc.accountId = :uuid")
    Optional<ClientDatabaseAccountDTO> findClientAccountInfoFromDB(@Param("uuid") String uuid);


    // Query to find account info from client_account table
    @Query("SELECT new com.example.msaccount.model.dto.admin.AdminDatabaseAccountDTO(acc.phone, acc.avatarUrl) " +
        "FROM account acc " +
        "JOIN admin_account ad ON acc.accountId = ad.accountId " +
        "WHERE acc.accountId = :uuid")
    Optional<AdminDatabaseAccountDTO> findAdminAccountInfoFromDB(@Param("uuid") String uuid);


//    Page<Account> findByAccountStatus(AccountStatusEnum se, PageRequest pageRequest);
}
