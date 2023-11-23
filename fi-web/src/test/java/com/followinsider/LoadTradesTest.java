package com.followinsider;

import com.followinsider.insider.*;
import com.followinsider.secapi.client.EdgarClient;
import com.followinsider.common.Color;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.utils.StringUtils;
import com.followinsider.utils.TerminalUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class LoadTradesTest {

    @Test
    void loadTrades() throws IOException, ParseException {
        EdgarClient client = new EdgarClient("Alexey Kovzel alexey.kovzel@gmail.com");
        InsiderFormRefLoader refLoader = new InsiderFormRefLoader(client);
        InsiderFormLoader loader = new InsiderFormLoader(client);

        List<FormRef> refs = refLoader.loadLatest(0, 200);
        System.out.println("Loading refs: " + refs.size());

        for (FormRef ref : refs) {
            loader.loadFormByRef(ref).ifPresent(LoadTradesTest::printTrades);
        }
    }

    private static void printTrades(InsiderForm form) {
        for (String prettyTrade : getPrettyTrades(form)) {
            System.out.println(prettyTrade);
        }
    }

    public static List<String> getPrettyTrades(InsiderForm form) {
        List<String> values = new ArrayList<>();
        String format = "%s at %s %s %s shares for $%s on %s";

        for (InsiderTrade trade : form.getTrades()) {
            String actionValue = trade.getType() == InsiderTradeType.BUY
                    ? TerminalUtils.color("bought", Color.GREEN)
                    : TerminalUtils.color("sold", Color.RED);

            values.add(String.format(format,
                    form.getInsider().getName(),
                    form.getIssuer().getIssuerName(),
                    actionValue,
                    StringUtils.stringify(trade.getShareNum()),
                    StringUtils.stringify(trade.getSharePrice()),
                    StringUtils.stringify(trade.getExecutedAt())
            ));
        }
        return values;
    }

}
