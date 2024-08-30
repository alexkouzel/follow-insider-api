package com.followinsider.common.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SearchRequest(

    @NotNull
    @Size(min = 1, max = 200)
    String text,

    @Min(1)
    @Max(10)
    int limit

) {}
