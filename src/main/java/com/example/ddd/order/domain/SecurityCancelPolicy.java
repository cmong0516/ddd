package com.example.ddd.order.domain;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCancelPolicy implements CancelPolicy{
    @Override
    public boolean hasCancellationPermission(Order order, Canceller canceller) {
        return isCancellerOrderer(order, canceller) || isCurrentUserAdminRole();
    }

    private boolean isCancellerOrderer(Order order, Canceller canceller) {
        return isCancellerOrderer(order, canceller) || isCurrentUserAdminRole();
    }

    private boolean isCurrentUserAdminRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) {
            return false;
        }

        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
