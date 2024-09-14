package com.example.msaccount.mapper;

//import com.example.msaccount.entity.Account;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.dto.AccountWithPropertiesDTO;
import com.example.msaccount.model.dto.PropertyDTO;
import com.example.msaccount.utils.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
//    AccountDTO toAccountDTO(Account account);
}
