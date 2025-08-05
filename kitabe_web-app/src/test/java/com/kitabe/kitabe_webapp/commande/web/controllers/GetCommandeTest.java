package com.kitabe.kitabe_webapp.commande.web.controllers;

import com.kitabe.kitabe_webapp.commande.AbstractIT;
import com.kitabe.kitabe_webapp.commande.MockWithOAuth2Users;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetCommandeTest extends AbstractIT {

    @Test
    @MockWithOAuth2Users(username = "user")
    void shouldReturnCommandeReussi() throws Exception {
        mockMvc.perform(get("/api/commande")).andExpect(status().isOk());
    }
}
