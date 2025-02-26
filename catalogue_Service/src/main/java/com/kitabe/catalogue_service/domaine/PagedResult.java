package com.kitabe.catalogue_service.domaine;

import java.util.List;

public record PagedResult<T>(
    List<T> data,
    Long totalElements,
    int pageNumber,
    int totalPages,
    boolean isFirst,
    boolean isLast,
    boolean hasNext,
    boolean hasPrevious){}
