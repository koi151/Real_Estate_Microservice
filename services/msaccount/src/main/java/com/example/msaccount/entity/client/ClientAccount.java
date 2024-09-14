//package com.example.msaccount.entity.client;
//
//import com.example.msaccount.entity.Account;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.PositiveOrZero;
//import lombok.*;
//
//@Entity(name = "client_account")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ClientAccount {
//
//    @Id
//    private Long accountId;
//
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "account_id")
//    private Account account;
//
//    @PositiveOrZero(message = "Balance must equal or above zero")
//    @NotNull(message = "Account's balance cannot be null")
//    private Double balance;
//}
