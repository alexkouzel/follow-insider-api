package com.followinsider.secapi.forms.refs.daily;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyFeedFactory {

    public static DailyFeed build() throws ParseException {
        List<DailyForm> forms = new ArrayList<>();
        Date date = DateUtils.parse("08-17-2023", "MM-dd-yyyy");
        forms.add(new DailyForm("0001213900-23-068538", "1599407", "1847 Holdings LLC", FormType.F4, date));
        forms.add(new DailyForm("0001420506-23-001727", "1599407", "1847 Holdings LLC", FormType.F4, date));
        forms.add(new DailyForm("0000902664-23-004502", "1822912", "26 Capital Acquisition Corp.", FormType.F4, date));
        return new DailyFeed(forms);
    }

}
