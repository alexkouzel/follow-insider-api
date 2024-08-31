package com.followinsider.common.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record GetPageRequest(

    @Min(0)
    int pageIdx,

    @Min(1)
    @Max(100)
    int pageSize

) {

    public Pageable prepare() {
        return PageRequest.of(pageIdx, pageSize);
    }

    public Pageable prepare(Sort sort) {
        return PageRequest.of(pageIdx, pageSize, sort);
    }

}
