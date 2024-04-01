package com.memori.memori_service.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedDto<T> {
    private Integer totalPages;

    private Long totalElements;

    private Integer currentPageNumber;

    private Boolean hasNextPage;

    private Boolean hasPreviousPage;

    private List<T> content;
}