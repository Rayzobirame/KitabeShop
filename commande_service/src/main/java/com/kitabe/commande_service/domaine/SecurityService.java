package com.kitabe.commande_service.domaine;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUsername() {
        return "user";
    }
}
