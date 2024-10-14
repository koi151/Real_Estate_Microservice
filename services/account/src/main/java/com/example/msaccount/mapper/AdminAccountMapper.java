package com.example.msaccount.mapper;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import com.example.msaccount.utils.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {StringUtil.class})
public interface AdminAccountMapper {

    List<AdminAccountDTO> toAdminAccountDTOs(Page<AdminAccount> adminAccounts);

}
