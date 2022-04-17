package org.trdc.announcement.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Author Ibrahim Jamali
 */
public class Util {

	private static final Logger logger = LogManager.getLogger(Util.class);

	public static String rand(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();

	}

	public static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}

	public static void putInFile(String fileName, String message) {
		Path path = Paths.get(fileName);
		try (PrintStream out = new PrintStream(new FileOutputStream(path.toFile(), true))) {
			if (!Files.exists(path))
				Files.createFile(path);
			out.println(message);
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void putHashSetInFile(String fileName, HashSet<String> lines) {
		Path path = Paths.get(fileName);
		try {
			if (!Files.exists(path))
				Files.createFile(path);

			Files.write(path, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			logger.error("putListInFile error :" + e.getMessage());
		}
	}

	public static Set<String> getHashSetFromFile(String fileName) {
		HashSet<String> announcements = new HashSet<String>();
		try {
			Path path = Paths.get(fileName);
			announcements.addAll(Files.readAllLines(path, Charset.forName("UTF-8")));
		} catch (IOException e) {
			logger.error("getListFromFile error :" + e.getMessage());
		}
		return announcements;
	}

	public static Properties loadConfigurtion() {
		String fileName = "resources/config.properties";
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
			properties.load(input);
		} catch (IOException e) {
			logger.error("Properties file " + fileName + " error :" + e.getMessage());
		}
		return properties;
	}

	public static String generateToken() {

		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";

		int stringLength = 11;

		String randomString = "";

		for (int i = 0; i < stringLength; i++) {

			int rnum = (int) Math.floor(Math.random() * chars.length());

			randomString += chars.substring(rnum, rnum + 1);

		}
		String token = "";
		try {
			token = URLEncoder.encode((randomString), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return token;

	}

	public static CookieStore getCookieStore(Path cookieFile) {
		try (FileInputStream fileStream = new FileInputStream(cookieFile.toFile());
				ObjectInputStream objStream = new ObjectInputStream(fileStream)) {
			CookieStore cookieStore = (CookieStore) objStream.readObject();
			return cookieStore;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new BasicCookieStore();
		}

	}

	public static void setCookieStore(Path cookieFile, CookieStore cookieStore) {
		try (FileOutputStream file = new FileOutputStream(cookieFile.toFile());
				ObjectOutputStream output = new ObjectOutputStream(file)) {
			output.writeObject(cookieStore);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

	}

	public static String encode(String string) {
		return Base64.getEncoder().encodeToString(string.getBytes());
	}

	public static String decode(String string) {
		return new String(Base64.getDecoder().decode(string.getBytes()));
	}

	public Util() {
		twitter("saksjdk dskjdksjds dskjdksjdks dskdjskjdksjd dskdjsd");
	}

	public static void main(String[] args) {
		new Util();
	}

	public static void twitter(String message) {
		Properties config = Util.loadConfigurtion();
		String consumerKey = config.getProperty("consumerKey");
		String consumerSecret = config.getProperty("consumerSecret");
		String oAuthAccessToken = config.getProperty("oAuthAccessToken");
		String oAuthAccessTokenSecret = config.getProperty("oAuthAccessTokenSecret");

		try {
			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			confbuilder.setOAuthAccessToken(oAuthAccessToken).setOAuthAccessTokenSecret(oAuthAccessTokenSecret)
					.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret);
			Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

			twitter.updateStatus(message);

		} catch (Exception e) {
			logger.error("an Error has been occured in twitter message handler " + e.getMessage());
		}

	}

	public static void telegram(Client client, String message) {

		Properties config = Util.loadConfigurtion();
		String token = config.getProperty("telegram_token");
		String userId = config.getProperty("telegram_channel_id");
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("Accept-Language", "en-US,en;q=0.5");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");

		try {
			String url = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id="
					+ URLEncoder.encode(userId, "UTF-8") + "&parse_mode=HTML&disable_web_page_preview=true&text="
					+ URLEncoder.encode(message, "UTF-8");
			client.doGet(url, "https://google.fr", headers).getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("there is a problem when try to send telegram message caused by : " + e.getMessage());
		}
	}
}
