package com.followinsider.secapi.forms.refs.daily;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DailyFeed {

    private List<DailyForm> forms;

}
