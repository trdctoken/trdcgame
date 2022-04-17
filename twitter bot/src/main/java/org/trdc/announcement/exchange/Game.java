package org.trdc.announcement.exchange;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.trdc.announcement.utils.Client;
import org.trdc.announcement.utils.Transaction;
/**
 * Author Ibrahim Jamali
 */
public class Game extends TRDC {

	public Game(Client client, Properties properties) {
		super(client, properties);
		setCondition("table-hover");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		headers.put("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,fr;q=0.7,ar;q=0.6,es;q=0.5,de;q=0.4");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Cookie",
				"ASP.NET_SessionId=2qcpxy3a5y2rlrzw0smiqr25; __cflb=02DiuJNoxEYARvg2sN4zbncfn2GL25Upg16LtsiNuRjpQ; bscscan_cookieconsent=True; __cf_bm=PqfhMTJJxmNfcEKF9S4oPGn.7BQxfd9bovoj1XOQOTE-1650090267-0-AWC1ZGlT138fT0bZtJnKWMBZjll7C55fNVh55Jb6Fg98IoyLas/iMzZ2caikONUSyzeIpigg9vmfR5yxXxBCnIK3SEQA01F+iTF211GEctN8WnrFX8YzprDe3S73bybJeQ==; __stripe_mid=9f92ca8c-2cf9-4e0e-826f-e3d04149fb7fff3acb; __stripe_sid=b44bb248-02d7-41a4-ad8d-153ef80a4888fe5e67; amp_fef1e8=2126364d-f13d-41e0-811e-f71b37abeb9aR...1g0ogn00a.1g0oh73ld.1b.2.1d; ASP.NET_SessionId=bvvvtg3mqwtpsig5ypw3iwxy; __cflb=0H28vyb6xVveKGjdV3CYUMgiti5JgVsUZsnudDNMWuz");
		headers.put("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"");
		headers.put("sec-ch-ua-mobile", "?0");
		headers.put("sec-ch-ua-platform", "\"macOS\"");
		headers.put("Sec-Fetch-Dest", "document");
		headers.put("Sec-Fetch-Mode", "navigate");
		headers.put("Sec-Fetch-Site", "same-origin");
		headers.put("Sec-Fetch-User", "?1");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
		try {
			return getClient().doGet(this.getGameExplorerUrl(), "https://bscscan.com", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public Set<Transaction> parse(String announcements) {
		Document doc = Jsoup.parse(announcements);

		Set<Transaction> transactions = new HashSet<>();

		Elements news = doc.select(".table-hover tr");
		for (int i = 1; i < news.size(); i++) {
			transactions.add(new Transaction(news.get(i).children()));
		}

		return transactions;
	}

}
