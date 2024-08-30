package com.followinsider.modules.trading.form;

import com.alexkouzel.filing.FilingUrlBuilder;
import com.followinsider.modules.trading.form.models.Form;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FormUtils {

    public String getXmlUrl(Form form) {
        return FilingUrlBuilder.buildXmlUrl(
            form.getCompany().getCik(),
            form.getAccNo(),
            form.getXmlFilename());
    }

}
