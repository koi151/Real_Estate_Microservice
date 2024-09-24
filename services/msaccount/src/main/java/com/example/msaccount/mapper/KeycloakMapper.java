package com.example.msaccount.mapper;

import com.example.msaccount.model.dto.KeycloakUserDTO;
import com.example.msaccount.utils.StringUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {StringUtil.class})
public interface KeycloakMapper {

    @Mapping(target = "roleNames", source = "roleNames")
    @Mapping(target = "id", source = "userUUID")
    KeycloakUserDTO toKeycloakUserDTO(UserRepresentation userRep, List<String> roleNames, String userUUID);

}
