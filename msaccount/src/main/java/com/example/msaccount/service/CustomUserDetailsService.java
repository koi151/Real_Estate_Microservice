package com.example.msaccount.service;

import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.Permission;
import com.example.msaccount.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository adminAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        Account account = adminAccountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = account.getRole().getPermissionsEntities().stream()
                .map(Permission::getName)
                .map(permissionEnum -> new SimpleGrantedAuthority(permissionEnum.toString()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
//                account.isEnabled(), // account enabled status
//                true, // account non-expired
//                true, // credentials non-expired
//                true, // account non-locked
                authorities
        );
    }
}
