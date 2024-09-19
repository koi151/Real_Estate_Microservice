package com.example.msaccount.mapper;

//import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.Account;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.utils.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {StringUtil.class})
public interface AccountMapper {
//    @Mapping(source = "account.accountId", target = "accountId")
//    @Mapping(source = "account.accountName", target = "accountName")
//    @Mapping(source = "account.avatarUrl", target = "avatarUrl")
//    @Mapping(source = "properties", target = "properties")
//    AccountWithPropertiesDTO toAccountWithPropertiesDTO(Account account, List<PropertyDTO> properties);
//
//    @Mapping(source = "role.name", target = "role")
//    AccountWithNameAndRoleDTO toAccountWithNameAndRoleDTO(Account account);
//
//    @Mapping(target = "role", source = "role.name")
//    @Mapping(target = "accountStatus", source = "accountStatus.status")
//    @Mapping(target = "accountType", source = "role.accountType.accountType")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "isAdmin", source = "account.isAdmin")
    AccountDTO toAccountDTO(AccountCreateRequest account, String avatarUrl);

    @Mapping(target = "userId", source = "id")
    Account toAccountEntity(AccountCreateRequest request, String id);

}
