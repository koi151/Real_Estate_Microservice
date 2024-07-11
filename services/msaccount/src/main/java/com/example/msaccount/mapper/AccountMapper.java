package com.example.msaccount.mapper;

import com.example.msaccount.entity.Account;
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
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.accountName", target = "accountName")
    @Mapping(source = "account.avatarUrl", target = "avatarUrl")
    @Mapping(source = "properties", target = "properties")
    AccountWithPropertiesDTO toAccountWithPropertiesDTO(Account account, List<PropertyDTO> properties);
}
