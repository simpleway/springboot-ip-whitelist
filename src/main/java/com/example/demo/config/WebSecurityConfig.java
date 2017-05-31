package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * Override default spring-boot SecurityAutoConfiguration and SpringBootWebSecurityConfiguration
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.filter.allowedIPs}")
    String allowedIPs;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        IPWhitelistPreAuthenticatedProcessingFilter ipWhitelistFilter = new IPWhitelistPreAuthenticatedProcessingFilter();
        ipWhitelistFilter.setAuthenticationManager(authenticationManagerBean()); // authnMgr hasn't built yet
        ipWhitelistFilter.setAllowedIPs(allowedIPs);

        http
            .antMatcher("/protected")
            .addFilterAt(ipWhitelistFilter, AbstractPreAuthenticatedProcessingFilter.class);

        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(ipWhitelistFilter));
        http.authenticationProvider(preAuthProvider);

        http
            .authorizeRequests()
                .antMatchers("/protected").authenticated()
            .and()
            .httpBasic();
    }
}
