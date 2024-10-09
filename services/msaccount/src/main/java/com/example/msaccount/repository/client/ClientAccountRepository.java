package com.example.msaccount.repository.client;

import com.example.msaccount.entity.client.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, String> {

    @Modifying
    @Query("UPDATE client_account ca SET ca.deleted = true WHERE ca.accountId = :id")
    int softDeleteClientAccountByAccountId(@Param("id") String id);

}
