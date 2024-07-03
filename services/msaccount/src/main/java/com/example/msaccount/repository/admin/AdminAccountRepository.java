package com.example.msaccount.repository.admin;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {
    @Query("SELECT aa FROM admin_account aa JOIN aa.account a WHERE a.deleted = :deleted")
    List<AdminAccount> findAllByAccountDeleted(@Param("deleted") boolean deleted, Sort sort);

    @Query("SELECT aa FROM admin_account aa JOIN aa.account a WHERE a.accountStatus = :status AND a.deleted = :deleted")
    List<AdminAccount> findByAccountStatusDeleted(@Param("deleted")AccountStatusEnum status,
                                                  @Param("deleted") boolean deleted, Sort sort);
}
