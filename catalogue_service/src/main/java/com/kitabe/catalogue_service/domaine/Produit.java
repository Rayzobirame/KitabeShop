package com.kitabe.catalogue_service.domaine;

import java.math.BigDecimal;

public record Produit(String code, String nom, String description, String images, BigDecimal prix) {}
