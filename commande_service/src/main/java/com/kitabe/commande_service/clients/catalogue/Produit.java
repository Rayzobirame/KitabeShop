package com.kitabe.commande_service.clients.catalogue;

import java.math.BigDecimal;

public record Produit(String code , String nom, BigDecimal prix,String image_url, String description) {
}
