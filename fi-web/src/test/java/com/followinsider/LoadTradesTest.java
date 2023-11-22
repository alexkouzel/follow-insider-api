package com.followinsider;

import com.followinsider.secapi.client.EdgarClient;
import com.followinsider.secapi.forms.insider.InsiderForm;
import com.followinsider.secapi.forms.insider.InsiderFormLoader;
import com.followinsider.secapi.forms.insider.InsiderFormRefLoader;
import com.followinsider.secapi.forms.refs.FormRef;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

class LoadTradesTest {

	@Test
	void loadTrades() throws IOException, ParseException {
		EdgarClient client = new EdgarClient("Alexey Kovzel alexey.kovzel@gmail.com");
		InsiderFormRefLoader refLoader = new InsiderFormRefLoader(client);
		InsiderFormLoader loader = new InsiderFormLoader(client);
		List<FormRef> refs = refLoader.loadDaysAgo(2);

		System.out.println("Loading refs: " + refs.size());
		long startTime = System.currentTimeMillis();

		for (FormRef ref : refs) {
			loader.loadForm(ref).ifPresent(LoadTradesTest::printTrades);
		}

		double timePassed = (System.currentTimeMillis() - startTime) / 1000.0;
		System.out.println("Time passed: " + timePassed + " sec");
		System.out.println("Time per round: " + refs.size() / timePassed + " sec");
	}

	private static void printTrades(InsiderForm form) {
		for (String prettyTrade : form.getPrettyTrades()) {
			System.out.println(prettyTrade);
		}
	}

}
