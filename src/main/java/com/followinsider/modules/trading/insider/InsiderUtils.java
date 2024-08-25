package com.followinsider.modules.trading.insider;

import com.followinsider.modules.trading.insider.models.Insider;
import com.followinsider.modules.trading.insider.models.InsiderDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InsiderUtils {

    public InsiderDto toDto(Insider insider) {
        return new InsiderDto(
                insider.getCik(),
                insider.getName()
        );
    }

}
