package com.followinsider.secapi.forms.refs.index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexFeed {

    private List<IndexForm> forms;

    private String description;

    private String comments;

    private String anonymousFTP;

    private String cloudHTTP;

    private Date updatedAt;

}
