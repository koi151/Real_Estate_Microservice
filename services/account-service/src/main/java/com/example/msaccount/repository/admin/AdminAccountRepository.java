package com.example.msaccount.repository.admin;

import com.example.msaccount.entity.admin.AdminAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {

    @Modifying
    @Query("UPDATE admin_account aa SET aa.deleted = true WHERE aa.accountId = :id")
    int softDeleteAdminAccountById(@Param("id") String id);

    @EntityGraph(value = "AdminAccount.detail", type = EntityGraph.EntityGraphType.FETCH)
    Page<AdminAccount> findAllByDeletedFalse(Pageable pageable);

//    @Query("SELECT aa FROM admin_account aa JOIN aa.account a WHERE a.deleted = :deleted")
//    List<AdminAccount> findAllByAccountDeleted(@Param("deleted") boolean deleted, Sort sort);
//
//    @Query("SELECT aa FROM admin_account aa JOIN aa.account a WHERE a.accountStatus = :status AND a.deleted = :deleted")
//    List<AdminAccount> findByAccountStatusDeleted(@Param("deleted") AccountStatusEnum status,
//                                                  @Param("deleted") boolean deleted,
//                                                  Sort sort);

//    Optional<AdminAccount> findByAccountIdDeleted(@Param("id") Long id,
//                                                  @Param("deleted") boolean deleted,
//                                                  Sort sort);
}

