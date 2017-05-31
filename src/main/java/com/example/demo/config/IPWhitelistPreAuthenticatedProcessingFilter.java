package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

public class IPWhitelistPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter implements UserDetailsService {

    private String[] allowedIPs = new String[0];

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        for (String ip : allowedIPs) {
            if (new IpAddressMatcher(ip).matches(request)) {
                return request.getRemoteAddr();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Remote IP " + request.getRemoteAddr() + "not in allowed list: " + allowedIPs);
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "N/A", Collections.emptyList());
    }

    public void setAllowedIPs(String whitelistIPs) {
        if (!StringUtils.isEmpty(whitelistIPs)) {
            allowedIPs = whitelistIPs.split(",");
        }
    }
}
