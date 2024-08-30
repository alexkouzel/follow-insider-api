package com.followinsider.common.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetPageRequest(

    @Min(0)
    int pageIdx,

    @Min(1)
    @Max(100)
    int pageSize

) {

    public int offset() {
        return pageIdx * pageSize;
    }

}
