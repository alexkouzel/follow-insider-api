package com.followinsider.common.models.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageRequestDto(

        @Min(0)
        int pageIdx,

        @Min(1)
        @Max(100)
        int pageSize

) {}
