package com.kitabe.kitabe_webapp.catalogue;

import java.math.BigDecimal;

public record Produit(String code, String nom, String description, String images, BigDecimal prix) {}
