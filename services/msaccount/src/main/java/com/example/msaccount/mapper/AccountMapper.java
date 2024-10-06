package com.example.msaccount.mapper;

import com.example.msaccount.entity.Account;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.dto.ClientDatabaseAccountDTO;
import com.example.msaccount.model.dto.KeycloakUserDTO;
import com.example.msaccount.model.dto.admin.AdminDatabaseAccountDTO;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.utils.StringUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

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
    AccountWithNameAndRoleDTO toAccountWithNameAndRoleDTO(Account account);


    @Mapping(target = "avatarUrl", source = "account.avatarUrl")
    @Mapping(target = "accountEnabled", source = "account.accountEnable")
    AccountDTO requestToAccountDTO(Account account, KeycloakUserDTO kcUserDTO);

    @Mapping(target = "avatarUrl", source = "account.avatarUrl")
    @Mapping(target = "accountEnabled", source = "account.accountEnable")
    AccountDTO entityToAccountDTO(Account account, KeycloakUserDTO kcDTO);

    @Mapping(target = "accountId", source = "id")
    Account toAccountEntity(AccountCreateRequest request, String id);

    @Mapping(target = "avatarUrl", source = "adminDTO.avatarUrl")
    @Mapping(target = "phone", source = "adminDTO.phone")
    AccountDTO mapAdminDatabaseAccountDTO(AdminDatabaseAccountDTO adminDTO, AccountDTO accountDTOFromRedis);

    @Mapping(target = "avatarUrl", source = "clientDTO.avatarUrl")
    @Mapping(target = "phone", source = "clientDTO.phone")
    AccountDTO mapClientDatabaseAccountDTO(ClientDatabaseAccountDTO clientDTO, AccountDTO accountDTOFromRedis);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateAccountFromRequest(AccountUpdateRequest request, @MappingTarget Account account);
}
