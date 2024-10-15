package com.example.msaccount.model.dto.admin;

import com.example.msaccount.model.dto.CommonDatabaseAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdminDatabaseAccountDTO extends CommonDatabaseAccountDTO {

    public AdminDatabaseAccountDTO(String phone, String avatarUrl) {
        super(phone, avatarUrl);
    }
}
