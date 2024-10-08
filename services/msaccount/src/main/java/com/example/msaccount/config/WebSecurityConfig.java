//package com.example.msaccount.config;
//
//import com.example.msaccount.enums.AccountTypeEnum;
//import com.example.msaccount.filters.JwtTokenFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@Configuration
//@EnableWebSecurity
//@EnableWebMvc
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtTokenFilter jwtTokenFilter;
//
//    @Value("${API_PREFIX}")
//    private String apiPrefix;
//
//    // Manage security filters, define security rules, configure authentication and authorization:
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable); // disable CSRF (Cross-Site Request Forgery)
////                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
////                .authorizeHttpRequests(requests -> {
////                    requests
////                            .requestMatchers( // allow access without authentication
////                                    String.format("%s/accounts/register", apiPrefix),
////                                    String.format("%s/admin/accounts/login", apiPrefix),
////                                    String.format("%s/admin/accounts**", apiPrefix)
////
////                            )
////                            .permitAll()
////                            .requestMatchers(HttpMethod.GET,
////                                    String.format("%s/property**", apiPrefix)).hasAnyAuthority("PROPERTY_VIEW")
////                            .requestMatchers(HttpMethod.POST,
////                                    String.format("%s/property**", apiPrefix)).hasAnyAuthority("PROPERTY_CREATE")
////                            .requestMatchers(HttpMethod.PATCH,
////                                    String.format("%s/property**", apiPrefix)).hasAnyAuthority("PROPERTY_EDIT")
////                            .requestMatchers(HttpMethod.DELETE,
////                                    String.format("%s/property**", apiPrefix)).hasAnyAuthority("PROPERTY_DEL")
////
////                            .requestMatchers(HttpMethod.GET,
////                                    String.format("%s/property-category**", apiPrefix)).hasAnyAuthority("PROPERTY_CATE_VIEW")
////                            .requestMatchers(HttpMethod.POST,
////                                    String.format("%s/property-category**", apiPrefix)).hasAnyAuthority("PROPERTY_CATE_CREATE")
////                            .requestMatchers(HttpMethod.PATCH,
////                                    String.format("%s/property-category**", apiPrefix)).hasAnyAuthority("PROPERTY_CATE_EDIT")
////                            .requestMatchers(HttpMethod.DELETE,
////                                    String.format("%s/property-category**", apiPrefix)).hasAnyAuthority("PROPERTY_CATE_DEL")
////
//////                            .requestMatchers(HttpMethod.GET,
//////                                    String.format("%s/accounts**", apiPrefix)).hasAnyAuthority("ACCOUNT_VIEW")
////
//////                            .requestMatchers(HttpMethod.GET,
//////                                    String.format("%s/admin/accounts**", apiPrefix)).hasAnyAuthority("ACCOUNT_VIEW")
//////
//////                            .requestMatchers(HttpMethod.POST,
//////                                    String.format("%s/accounts**", apiPrefix)).hasAnyAuthority("ACCOUNT_CREATE")
//////                            .requestMatchers(HttpMethod.PATCH,
//////                                    String.format("%s/accounts**", apiPrefix)).hasAnyAuthority("ACCOUNT_EDIT")
//////                            .requestMatchers(HttpMethod.DELETE,
//////                                    String.format("%s/accounts**", apiPrefix)).hasAnyAuthority("ACCOUNT_DEL")
////
////                            .anyRequest().authenticated(); // require authentication for other requests
////                });
//
//        return http.build();
//    }
//}
