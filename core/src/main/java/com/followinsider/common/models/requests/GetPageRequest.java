package com.followinsider.common.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record GetPageRequest(

    @Min(0)
    @NotNull
    int pageIdx,

    @Min(1)
    @Max(100)
    @NotNull
    int pageSize,

    @Min(0)
    Integer count

) {

    public Pageable prepare() {
        return PageRequest.of(pageIdx, pageSize);
    }

    public Pageable prepare(Sort sort) {
        return PageRequest.of(pageIdx, pageSize, sort);
    }

    public boolean shouldReverse() {
        return count != null && count > 1_000_000 && getOffset() > count / 2;
    }

    public int getOffset(boolean reverse) {
        return reverse
            ? count - getOffset() - pageSize
            : getOffset();
    }

    public int getOffset() {
        return pageIdx * pageSize;
    }

}
