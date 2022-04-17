package org.trdc.announcement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.trdc.announcement.exchange.Game;
import org.trdc.announcement.exchange.TRDC;
import org.trdc.announcement.utils.Client;
import org.trdc.announcement.utils.DefaultClient;
import org.trdc.announcement.utils.Util;

/**
 * Author Ibrahim Jamali
 */
public class App {
	public App(String[] args) throws IOException {
		Client client = new DefaultClient();
		Properties properties = Util.loadConfigurtion();
		List<TRDC> exchanges = new ArrayList<TRDC>();
		List<String> arguments = Arrays.asList(args);
		System.out.println(arguments);
		if (arguments.contains("all")) {
			exchanges.addAll(Arrays.asList(new Game(client, properties)));
		}
		if (arguments.contains("game")) {
			exchanges.add(new Game(client, properties));
		}

		if (arguments.contains("help")) {
			System.err.println("Announcement.jar burn");
		}

		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(exchanges.size());
		for (TRDC exchange : exchanges) {
			executorService.scheduleAtFixedRate(exchange, 0, 3, TimeUnit.MINUTES);
		}
	}

	public static void main(String[] args) throws Exception {
		new App(new String[] { "game" });
	}
}
