package com.followinsider.core.trading.form.dto;

import com.followinsider.forms.refs.FormRef;
import lombok.Data;

import java.util.Date;

@Data
public class FormDto {

    private String accNum;

    private Date filedAt;

    public FormDto(FormRef ref) {
        this.accNum = ref.accNum();
        this.filedAt = ref.filedAt();
    }

}
